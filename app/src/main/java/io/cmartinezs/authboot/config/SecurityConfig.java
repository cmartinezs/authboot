package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.api.AuthenticationEntryPointImpl;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.infra.security.service.JwtAuthUserDetailsService;
import io.cmartinezs.authboot.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Carlos
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final SecurityProperties securityProperties;
  private final AuthenticationEntryPointImpl authenticationEntryPoint;
  private final UserPersistencePort userPersistence;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new JwtAuthUserDetailsService(userPersistence);
  }

  @Bean
  public AuthenticationManager authManager(
      HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
      throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder)
        .and()
        .build();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    // Disable CSRF (cross site request forgery)
    httpSecurity
        .csrf()
        .disable()
        // Enabled cross-domain
        .cors()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        // No session will be created or used by spring security
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/jwt/login", "/jwt/encrypt/**", "/h2-console/**")
        .permitAll()
        // Disallow everything else..
        .anyRequest()
        .authenticated();

    // disable page caching, required to set for H2 else H2 Console will be blank.
    httpSecurity.headers().frameOptions().sameOrigin().cacheControl();

    // Optional, if you want to test the API from a browser
    if (securityProperties.isEnableHttpBasic()) {
      httpSecurity.httpBasic();
    }

    return httpSecurity.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web ->
        web.debug(false)
            .ignoring()
            .antMatchers("/actuator/**/**", "/error")
            // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in
            // production)
            .and()
            .ignoring()
            .antMatchers("/h2-console/**/**");
  }
}
