package io.cmartinezs.authboot.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** This class is a configuration class for the password encoder. */
@Configuration
@RequiredArgsConstructor
public class PasswordEncoderConfig {
  /**
   * This method creates a bean of type PasswordEncoder.
   *
   * @return a bean of type PasswordEncoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
