package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.core.adapter.service.JsonWebTokenServiceAdapter;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.core.properties.TokenProperties;
import io.cmartinezs.authboot.infra.security.service.AuthServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * @author Carlos
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class ServicePortConfig {

  private final AuthenticationManager authenticationManager;
  private final TokenProperties tokenProperties;

  @Bean
  public TokenServicePort tokenService() {
    return new JsonWebTokenServiceAdapter(tokenProperties);
  }

  @Bean
  public AuthServicePort authService() {
    return new AuthServiceAdapter(authenticationManager);
  }
}
