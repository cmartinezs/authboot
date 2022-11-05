package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.ControllerProperties;
import io.cmartinezs.authboot.api.response.BaseResponse;
import io.cmartinezs.authboot.api.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Carlos
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerHandler {
  private static final String COMMON_EXCEPTION_MESSAGE_LOG = "An exception has occurred: {}";
  private static final String COMMON_EXCEPTION_MESSAGE =
      "An error has occurred, please try again. If the problem persists please inform the email %s";
  private final ControllerProperties properties;

  @ExceptionHandler({Exception.class})
  public ResponseEntity<BaseResponse> exception(Exception exception) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    var format = String.format(COMMON_EXCEPTION_MESSAGE, properties.getNotificationEmail());
    var errorCode = "E00";
    var failure = new MessageResponse(errorCode, format);
    var response = BaseResponse.builder().failure(failure).build();
    addDebugDetailsIfIsEnabled(exception, response, errorCode);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<BaseResponse> authenticationException(AuthenticationException exception) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    var errorCode = "E00";
    var failure = new MessageResponse(errorCode, "Failed authentication");
    var response = BaseResponse.builder().failure(failure).build();
    addDebugDetailsIfIsEnabled(exception, response, errorCode);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler({DisabledException.class})
  public ResponseEntity<BaseResponse> disabled(DisabledException exception) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    var failure = new MessageResponse("E00", "Disabled user");
    var response = BaseResponse.builder().failure(failure).build();
    addDebugDetailsIfIsEnabled(exception, response, "E00");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler({AccountExpiredException.class})
  public ResponseEntity<BaseResponse> accountExpired(AccountExpiredException exception) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    var failure = new MessageResponse("E00", "Account expired");
    var response = BaseResponse.builder().failure(failure).build();
    addDebugDetailsIfIsEnabled(exception, response, "E00");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler({BadCredentialsException.class})
  public ResponseEntity<BaseResponse> badCredentials(BadCredentialsException exception) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    var failure = new MessageResponse("E00", "Bad credentials");
    var response = BaseResponse.builder().failure(failure).build();
    addDebugDetailsIfIsEnabled(exception, response, "E00");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler({CredentialsExpiredException.class})
  public ResponseEntity<BaseResponse> credentialsExpired(CredentialsExpiredException exception) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    var failure = new MessageResponse("E00", "Credentials expired");
    var response = BaseResponse.builder().failure(failure).build();
    addDebugDetailsIfIsEnabled(exception, response, "E00");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<BaseResponse> authenticationException(
      HttpMessageNotReadableException exception) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    var failure = new MessageResponse("E00", "Required request body is missing");
    var response = BaseResponse.builder().failure(failure).build();
    addDebugDetailsIfIsEnabled(exception, response, "E00");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<BaseResponse> authenticationException(
      HttpRequestMethodNotSupportedException exception) {
    logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
    var failure = new MessageResponse("E00", exception.getMessage());
    var response = BaseResponse.builder().failure(failure).build();
    addDebugDetailsIfIsEnabled(exception, response, "E00");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  private void addDebugDetailsIfIsEnabled(
      Exception initialCause, BaseResponse response, String errorCode) {
    if (logger.isDebugEnabled()) {
      var sbDebug = new StringBuilder().append(initialCause.getMessage());
      var sbThrowable = new StringBuilder().append(initialCause.getClass().getSimpleName());
      while (initialCause.getCause() instanceof Exception cause && !cause.equals(initialCause)) {
        sbDebug.append(" -> ").append(cause.getMessage());
        sbThrowable.append(" -> ").append(cause.getClass().getSimpleName());
        initialCause = cause;
      }
      response.setDebug(new MessageResponse(errorCode, sbDebug.toString()));
      response.setThrowable(sbThrowable.toString());
    }
  }
}
