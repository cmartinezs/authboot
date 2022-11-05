package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.core.adapter.service.JsonWebTokenServiceAdapter;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.core.properties.TokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Carlos
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {
  private final TokenProperties tokenProperties;

  @Bean
  public TokenServicePort tokenProvider() {
    return new JsonWebTokenServiceAdapter(tokenProperties);
  }
}
