package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.properties.ControllerProperties;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.core.exception.persistence.ExistsEntityException;
import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
import io.cmartinezs.authboot.core.exception.service.MismatchedPasswordException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * @author Carlos
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerHandler {
  public static final String ERROR_CODE_EXCEPTION = "E00";
  public static final String ERROR_CODE_REQUIRED_REQUEST = "E01";
  public static final String ERROR_CODE_REQUEST_METHOD_NOT_SUPPORTED = "E02";
  public static final String ERROR_CODE_METHOD_ARGUMENT_NOT_VALID = "E03";
  public static final String ERROR_CODE_EXISTS_ENTITY = "E04";
  public static final String ERROR_CODE_NOT_FOUND_ENTITY = "E05";
  public static final String ERROR_CODE_AUTHENTICATION_EXCEPTION = "EA1";
  public static final String ERROR_CODE_DISABLED_EXCEPTION = "EA2";
  public static final String ERROR_CODE_EXPIRED_EXCEPTION = "EA3";
  public static final String ERROR_CODE_BAD_CREDENTIALS = "EA4";
  public static final String ERROR_CODE_CREDENTIALS_EXPIRED = "EA5";
  public static final String ERROR_CODE_ACCESS_DENIED = "EA6";
  public static final String ERROR_CODE_CURRENT_PASSWORD = "EA7";
  public static final String ERROR_CODE_NO_RESOURCE_FOUND = "EA8";
  private static final String COMMON_EXCEPTION_MESSAGE_LOG = "An exception has occurred: {}";
  private static final String COMMON_EXCEPTION_MESSAGE =
      "An error has occurred, please try again. If the problem persists please inform the email %s";
  private final ControllerProperties properties;

  private static BaseResponse createFailureResponse(String errorCode, String message) {
    return createFailureResponse(errorCode, message, null);
  }

  private static BaseResponse createFailureResponse(String errorCode, String message, Object data) {
    return BaseResponse.builder()
        .failure(new MessageResponse(errorCode, message))
        .data(data)
        .build();
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<BaseResponse> exception(Exception exception) {
    var message = String.format(COMMON_EXCEPTION_MESSAGE, properties.getNotificationEmail());
    var response = createFailureResponse(ERROR_CODE_EXCEPTION, message);
    return handleException(exception, HttpStatus.INTERNAL_SERVER_ERROR, response);
  }

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<BaseResponse> authenticationException(AuthenticationException exception) {
    return createBaseResponseForFailure(
        HttpStatus.UNAUTHORIZED,
        exception,
        ERROR_CODE_AUTHENTICATION_EXCEPTION,
        "Failed authentication");
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<BaseResponse> accessDeniedException(AccessDeniedException exception) {
    return createBaseResponseForFailure(
        HttpStatus.FORBIDDEN, exception, ERROR_CODE_ACCESS_DENIED, "Access denied");
  }

  @ExceptionHandler({DisabledException.class})
  public ResponseEntity<BaseResponse> disabled(DisabledException exception) {
    return createBaseResponseForFailure(
        HttpStatus.UNAUTHORIZED, exception, ERROR_CODE_DISABLED_EXCEPTION, "Disabled user");
  }

  @ExceptionHandler({AccountExpiredException.class})
  public ResponseEntity<BaseResponse> accountExpired(AccountExpiredException exception) {
    return createBaseResponseForFailure(
        HttpStatus.UNAUTHORIZED, exception, ERROR_CODE_EXPIRED_EXCEPTION, "Account expired");
  }

  @ExceptionHandler({BadCredentialsException.class})
  public ResponseEntity<BaseResponse> badCredentials(BadCredentialsException exception) {
    return createBaseResponseForFailure(
        HttpStatus.UNAUTHORIZED, exception, ERROR_CODE_BAD_CREDENTIALS, "Bad credentials");
  }

  @ExceptionHandler({CredentialsExpiredException.class})
  public ResponseEntity<BaseResponse> credentialsExpired(CredentialsExpiredException exception) {
    return createBaseResponseForFailure(
        HttpStatus.UNAUTHORIZED, exception, ERROR_CODE_CREDENTIALS_EXPIRED, "Credentials expired");
  }

  @ExceptionHandler({MismatchedPasswordException.class})
  public ResponseEntity<BaseResponse> passwordNotMatchException(
      MismatchedPasswordException exception) {
    return createBaseResponseForFailure(
        HttpStatus.CONFLICT, exception, ERROR_CODE_CURRENT_PASSWORD, "Password not match");
  }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<BaseResponse> httpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    var response =
        createFailureResponse(ERROR_CODE_REQUIRED_REQUEST, "Required request body is missing");
    return handleException(exception, HttpStatus.BAD_REQUEST, response);
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<BaseResponse> httpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exception) {
    return createBaseResponseForFailure(
        HttpStatus.BAD_REQUEST,
        exception,
        ERROR_CODE_REQUEST_METHOD_NOT_SUPPORTED,
        "HTTP Method not supported for this endpoint");
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<BaseResponse> methodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    var data = extractFieldErrors(exception);
    var response =
        createFailureResponse(
            ERROR_CODE_METHOD_ARGUMENT_NOT_VALID, "Request validation error", data);
    return handleException(exception, HttpStatus.BAD_REQUEST, response);
  }

  @ExceptionHandler({ExistsEntityException.class})
  public ResponseEntity<BaseResponse> existsEntityException(ExistsEntityException exception) {
    return createBaseResponseForFailure(
        HttpStatus.CONFLICT, exception, ERROR_CODE_EXISTS_ENTITY, exception.getMessage());
  }

  @ExceptionHandler({NoResourceFoundException.class})
  public ResponseEntity<BaseResponse> noResourceFoundException(NoResourceFoundException exception) {
    return createBaseResponseForFailure(
        HttpStatus.NOT_FOUND, exception, ERROR_CODE_NO_RESOURCE_FOUND, "Resource not found");
  }

  @ExceptionHandler({NotFoundEntityException.class})
  public ResponseEntity<BaseResponse> notFoundEntityException(NotFoundEntityException exception) {
    return createBaseResponseForFailure(
        HttpStatus.NOT_FOUND, exception, ERROR_CODE_NOT_FOUND_ENTITY, exception.getMessage());
  }

  private ResponseEntity<BaseResponse> createBaseResponseForFailure(
      HttpStatus statusCode, Exception exception, String errorCode, String errorMessage) {
    var response = createFailureResponse(errorCode, errorMessage);
    return handleException(exception, statusCode, response);
  }

  private ResponseEntity<BaseResponse> handleException(
      Exception exception, HttpStatus status, BaseResponse response) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    addDebugDetailsIfIsEnabled(exception, response);
    return ResponseEntity.status(status).body(response);
  }

  /**
   * Add debug details if is enabled.
   *
   * @param initialCause the initial cause
   * @param response the response
   */
  private void addDebugDetailsIfIsEnabled(Exception initialCause, BaseResponse response) {
    if (logger.isDebugEnabled()) {
      logger.debug("Trace exception: ", initialCause);
      var sbDebug = new StringBuilder().append(initialCause.getMessage());
      var sbThrowable = new StringBuilder().append(initialCause.getClass().getSimpleName());
      while (initialCause.getCause() instanceof Exception cause && !cause.equals(initialCause)) {
        sbDebug.append(" -> ").append(cause.getMessage());
        sbThrowable.append(" -> ").append(cause.getClass().getSimpleName());
        initialCause = cause;
      }
      response.setDebug(new MessageResponse(response.getFailure().code(), sbDebug.toString()));
      response.setThrowable(sbThrowable.toString());
    }
  }

  /**
   * Extract field errors map.
   *
   * @param exception the exception
   * @return the map
   */
  private Map<String, String> extractFieldErrors(MethodArgumentNotValidException exception) {
    Function<FieldError, String> keyMapper = FieldError::getField;
    Function<FieldError, String> valueMapper =
        fieldError -> Objects.toString(fieldError.getRejectedValue());
    var mapCollector = Collectors.toMap(keyMapper, valueMapper);
    return exception.getFieldErrors().stream().collect(mapCollector);
  }
}
