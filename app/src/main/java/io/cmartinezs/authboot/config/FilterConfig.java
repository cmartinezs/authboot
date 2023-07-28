package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.security.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is a configuration class for the filters.
 */
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    /**
     * This method creates a bean of type JwtAuthorizationFilter.
     *
     * @param authService the AuthServicePort.
     * @param tokenService the TokenServicePort.
     * @return a bean of type JwtAuthorizationFilter.
     */
    @Bean
    public JwtAuthorizationFilter getTokenFilter(AuthServicePort authService, TokenServicePort tokenService) {
        return new JwtAuthorizationFilter(authService, tokenService);
    }
}