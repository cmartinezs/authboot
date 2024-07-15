package io.cmartinezs.authboot.app.config;

import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import io.cmartinezs.authboot.app.filters.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** This class is a configuration class for the filters. */
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

  /**
   * This method creates a bean of type JwtAuthorizationFilter.
   *
   * @param userService the UserServicePort.
   * @param tokenService the TokenServicePort.
   * @return a bean of type JwtAuthorizationFilter.
   */
  @Bean
  public JwtAuthorizationFilter getTokenFilter(
      UserServicePort userService, TokenServicePort tokenService) {
    return new JwtAuthorizationFilter(userService, tokenService);
  }
}
