package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.ControllerProperties;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.core.exception.PasswordNotMatchException;
import io.cmartinezs.authboot.core.exception.persistence.ExistsEntityException;
import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

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
    private static final String COMMON_EXCEPTION_MESSAGE_LOG = "An exception has occurred: {}";
    private static final String COMMON_EXCEPTION_MESSAGE =
            "An error has occurred, please try again. If the problem persists please inform the email %s";
    private final ControllerProperties properties;

    private static void logException(Exception exception) {
        if (logger.isDebugEnabled()) {
            logger.error(COMMON_EXCEPTION_MESSAGE_LOG, exception.getMessage());
        } else {
            logger.error("Trace exception", exception);
        }
    }

    private static BaseResponse createFailureResponse(String errorCode, String message) {
        return createFailureResponse(errorCode, message, null);
    }

    private static BaseResponse createFailureResponse(String errorCode, String message, Object data) {
        return BaseResponse.builder().failure(new MessageResponse(errorCode, message)).data(data).build();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<BaseResponse> exception(Exception exception) {
        logException(exception);
        var message = String.format(COMMON_EXCEPTION_MESSAGE, properties.getNotificationEmail());
        var response = createFailureResponse(ERROR_CODE_EXCEPTION, message);
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<BaseResponse> authenticationException(AuthenticationException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_AUTHENTICATION_EXCEPTION, "Failed authentication");
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<BaseResponse> accessDeniedException(AccessDeniedException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_ACCESS_DENIED, exception.getMessage());
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<BaseResponse> disabled(DisabledException exception) {
        logException(exception);
        BaseResponse response = createFailureResponse(ERROR_CODE_DISABLED_EXCEPTION, "Disabled user");
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({AccountExpiredException.class})
    public ResponseEntity<BaseResponse> accountExpired(AccountExpiredException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_EXPIRED_EXCEPTION, "Account expired");
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<BaseResponse> badCredentials(BadCredentialsException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_BAD_CREDENTIALS, "Bad credentials");
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({CredentialsExpiredException.class})
    public ResponseEntity<BaseResponse> credentialsExpired(CredentialsExpiredException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_CREDENTIALS_EXPIRED, "Credentials expired");
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler({PasswordNotMatchException.class})
    public ResponseEntity<BaseResponse> passwordNotMatchException(PasswordNotMatchException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_CREDENTIALS_EXPIRED, exception.getMessage());
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<BaseResponse> httpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_REQUIRED_REQUEST, "Required request body is missing");
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<BaseResponse> httpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_REQUEST_METHOD_NOT_SUPPORTED, exception.getMessage());
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<BaseResponse> methodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        logException(exception);
        var data = new HashMap<String, Object>();
        exception.getFieldErrors()
                .forEach(error -> data.put(error.getField(), error.getDefaultMessage()));
        var response = createFailureResponse(ERROR_CODE_METHOD_ARGUMENT_NOT_VALID, "Request validation error", data);
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({ExistsEntityException.class})
    public ResponseEntity<BaseResponse> existsEntityException(
            ExistsEntityException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_EXISTS_ENTITY, exception.getMessage());
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler({NotFoundEntityException.class})
    public ResponseEntity<BaseResponse> notFoundEntityException(
            NotFoundEntityException exception) {
        logException(exception);
        var response = createFailureResponse(ERROR_CODE_NOT_FOUND_ENTITY, exception.getMessage());
        addDebugDetailsIfIsEnabled(exception, response);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Add debug details if is enabled.
     *
     * @param initialCause the initial cause
     * @param response     the response
     */
    private void addDebugDetailsIfIsEnabled(
            Exception initialCause, BaseResponse response) {
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
}
