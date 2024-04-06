package io.cmartinezs.authboot.app.config;

import io.cmartinezs.authboot.core.adapter.service.RoleServiceAdapter;
import io.cmartinezs.authboot.core.adapter.service.UserServiceAdapter;
import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.core.port.service.*;
import io.cmartinezs.authboot.core.utils.property.UserServiceProperties;
import io.cmartinezs.authboot.infra.adapter.service.AuthServiceAdapter;
import io.cmartinezs.authboot.infra.adapter.service.JsonWebTokenServiceAdapter;
import io.cmartinezs.authboot.infra.adapter.service.PasswordEncoderServiceAdapter;
import io.cmartinezs.authboot.infra.adapter.service.email.EmailServiceAdapter;
import io.cmartinezs.authboot.infra.properties.email.EmailServiceProperties;
import io.cmartinezs.authboot.infra.utils.properties.TokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;

/** This class is a configuration class for the service ports. */
@Configuration
@RequiredArgsConstructor
public class ServicePortConfig {
  private final JavaMailSender javaMailSender;
  private final TemplateEngine emailTemplateEngine;
  private final EmailServiceProperties emailServiceProperties;

  /**
   * This method creates a bean of type TokenServicePort.
   *
   * @param tokenProperties the TokenProperties.
   * @return a bean of type TokenServicePort.
   */
  @Bean
  public TokenServicePort tokenService(TokenProperties tokenProperties) {
    return new JsonWebTokenServiceAdapter(tokenProperties);
  }

  /**
   * This method creates a bean of type AuthServicePort.
   *
   * @param authenticationManager the AuthenticationManager.
   * @return a bean of type AuthServicePort.
   */
  @Bean
  public AuthServicePort authService(AuthenticationManager authenticationManager) {
    return new AuthServiceAdapter(authenticationManager);
  }

  /**
   * This method creates a bean of type UserServicePort.
   *
   * @param userPersistence the UserPersistencePort.
   * @param passwordEncoderService the PasswordEncoderServicePort.
   * @param userServiceProperties the UserServiceProperties.
   * @return a bean of type UserServicePort.
   */
  @Bean
  public UserServicePort userService(
      UserPersistencePort userPersistence,
      PasswordEncoderServicePort passwordEncoderService,
      UserServiceProperties userServiceProperties) {
    return new UserServiceAdapter(
        userPersistence, passwordEncoderService, emailServicePort(), userServiceProperties);
  }

  /**
   * This method creates a bean of type PasswordEncoderServicePort.
   *
   * @param passwordEncoder The password encoder.
   * @return a bean of type PasswordEncoderServicePort.
   */
  @Bean
  public PasswordEncoderServicePort passwordEncoderServicePort(PasswordEncoder passwordEncoder) {
    return new PasswordEncoderServiceAdapter(passwordEncoder);
  }

  /**
   * This method creates a bean of type RoleServicePort.
   *
   * @param rolePersistence the RolePersistencePort.
   * @return a bean of type RoleServicePort.
   */
  @Bean
  public RoleServicePort roleService(RolePersistencePort rolePersistence) {
    return new RoleServiceAdapter(rolePersistence);
  }

  @Bean
  public EmailServicePort emailServicePort() {
    return new EmailServiceAdapter(javaMailSender, emailTemplateEngine, emailServiceProperties);
  }
}
