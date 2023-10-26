package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.api.AuthenticationEntryPointImpl;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.UserRepository;
import io.cmartinezs.authboot.infra.security.JwtAuthUserDetailsService;
import io.cmartinezs.authboot.infra.utils.properties.SecurityProperties;
import io.cmartinezs.authboot.security.JwtAuthorizationFilter;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class is a configuration class for the security.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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
        return new JwtAuthUserDetailsService(userRepository);
    }

    /**
     * This method creates a bean of type AuthenticationManager.
     *
     * @param http               the HttpSecurity.
     * @param passwordEncoder    the PasswordEncoder.
     * @param userDetailsService the UserDetailsService.
     * @return a bean of type AuthenticationManager.
     * @throws Exception if an error occurs.
     */
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

    /**
     * This method creates a bean of type SecurityFilterChain.
     *
     * @param httpSecurity             the HttpSecurity.
     * @param filter                   the JwtAuthorizationFilter.
     * @param securityProperties       the SecurityProperties.
     * @param authenticationEntryPoint the AuthenticationEntryPointImpl.
     * @return a bean of type SecurityFilterChain.
     * @throws Exception if an error occurs.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, JwtAuthorizationFilter filter
            , SecurityProperties securityProperties, AuthenticationEntryPointImpl authenticationEntryPoint) throws Exception {
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
                .antMatchers("/auth/**", "/h2-console/**")
                .permitAll()
                // Disallow everything else...
                .anyRequest()
                .authenticated();

        // Custom JWT based security filter
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching, required to set for H2 else H2 Console will be blank.
        httpSecurity.headers().frameOptions().sameOrigin().cacheControl();

        // Optional, if you want to test the API from a browser
        if (securityProperties.isEnableHttpBasic()) {
            httpSecurity.httpBasic();
        }

        return httpSecurity.build();
    }

    /**
     * This method creates a bean of type WebSecurityCustomizer.
     *
     * @return a bean of type WebSecurityCustomizer.
     */
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
