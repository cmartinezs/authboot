package io.cmartinezs.authboot.api.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

  @Serial private static final long serialVersionUID = -8970718410437077606L;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    // This is invoked when user tries to access a secured REST resource without supplying any
    // credentials
    // We should just send a 401 Unauthorized response because there is no 'login page' to redirect
    // to
    logger.error("Unauthorized: {} - URI: {}", authException.getMessage(), request.getRequestURI());
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }
}
