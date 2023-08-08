package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.core.adapter.service.UserServiceAdapter;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.core.port.service.*;
import io.cmartinezs.authboot.infra.adapter.service.AuthServiceAdapter;
import io.cmartinezs.authboot.infra.adapter.service.JsonWebTokenServiceAdapter;
import io.cmartinezs.authboot.infra.adapter.service.PasswordEncoderServiceAdapter;
import io.cmartinezs.authboot.infra.utils.properties.TokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class is a configuration class for the service ports.
 */
@Configuration
@RequiredArgsConstructor
public class ServicePortConfig {
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
     * @param userService           the UserServicePort.
     * @return a bean of type AuthServicePort.
     */
    @Bean
    public AuthServicePort authService(AuthenticationManager authenticationManager, UserServicePort userService) {
        return new AuthServiceAdapter(authenticationManager, userService);
    }

    /**
     * This method creates a bean of type UserServicePort.
     *
     * @param userPersistence        the UserPersistencePort.
     * @param passwordEncoderService the PasswordEncoderServicePort.
     * @return a bean of type UserServicePort.
     */
    @Bean
    public UserServicePort userService(UserPersistencePort userPersistence
            , PasswordEncoderServicePort passwordEncoderService, UserServiceProperties userServiceProperties) {
        return new UserServiceAdapter(userPersistence, passwordEncoderService, userServiceProperties);
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
}
