package io.cmartinezs.authboot.app.config;

import io.cmartinezs.authboot.api.security.AuthenticationEntryPointImpl;
import io.cmartinezs.authboot.app.filters.JwtAuthorizationFilter;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.UserRepository;
import io.cmartinezs.authboot.infra.security.DefaultUserDetailsService;
import io.cmartinezs.authboot.infra.utils.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/** This class is a configuration class for the security. */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  /**
   * This method creates a bean of type UserDetailsService.
   *
   * @param userRepository the UserRepository.
   * @return a bean of type UserDetailsService.
   */
  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return new DefaultUserDetailsService(userRepository);
  }

  @Bean
  public AuthenticationProvider authenticationProvider(
      PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * This method creates a bean of type SecurityFilterChain.
   *
   * @param httpSecurity the HttpSecurity.
   * @param jwtAuthorizationFilter the JwtAuthorizationFilter.
   * @param securityProperties the SecurityProperties.
   * @param authenticationEntryPoint the AuthenticationEntryPointImpl.
   * @return a bean of type SecurityFilterChain.
   * @throws Exception if an error occurs.
   */
  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity httpSecurity,
      JwtAuthorizationFilter jwtAuthorizationFilter,
      SecurityProperties securityProperties,
      AuthenticationEntryPointImpl authenticationEntryPoint,
      AuthenticationProvider authenticationProvider,
      CorsConfigurationSource corsConfigurationSource)
      throws Exception {

    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource))
        .headers(SecurityConfig::headerConfiguration)
        .sessionManagement(SecurityConfig::sessionManagementConfiguration)
        .authorizeHttpRequests(SecurityConfig::authorizationHttpRequestsConfiguration)
        .authenticationProvider(authenticationProvider)
        .exceptionHandling(getExceptionHandlingConfigurerCustomizer(authenticationEntryPoint))
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    // Optional, if you want to test the API from a browser
    if (securityProperties.isEnableHttpBasic()) {
      httpSecurity.httpBasic(Customizer.withDefaults());
    }

    return httpSecurity.build();
  }

  private static Customizer<ExceptionHandlingConfigurer<HttpSecurity>>
      getExceptionHandlingConfigurerCustomizer(
          AuthenticationEntryPointImpl authenticationEntryPoint) {
    return handlingConfigurer ->
        handlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
  }

  private static void authorizationHttpRequestsConfiguration(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          registry) {
    registry
        .requestMatchers(
            "/auth/**",
            "/h2-console/**",
            "/users/by-username/{username}/password-recovery/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/error",
        "/actuator/**")
        .permitAll()
        .anyRequest()
        .authenticated();
  }

  private static void sessionManagementConfiguration(
      SessionManagementConfigurer<HttpSecurity> manager) {
    manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  private static void headerConfiguration(HeadersConfigurer<HttpSecurity> headers) {
    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
  }
}
