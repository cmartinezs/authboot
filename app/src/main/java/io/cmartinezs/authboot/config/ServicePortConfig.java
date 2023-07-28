package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.core.adapter.service.UserServiceAdapter;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import io.cmartinezs.authboot.infra.adapter.service.AuthServiceAdapter;
import io.cmartinezs.authboot.infra.adapter.service.JsonWebTokenServiceAdapter;
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
     * @param passwordEncoder       the PasswordEncoder.
     * @param userService           the UserServicePort.
     * @return a bean of type AuthServicePort.
     */
    @Bean
    public AuthServicePort authService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder
            , UserServicePort userService) {
        return new AuthServiceAdapter(authenticationManager, passwordEncoder, userService);
    }

    /**
     * This method creates a bean of type UserServicePort.
     *
     * @param userPersistencePort the UserPersistencePort.
     * @return a bean of type UserServicePort.
     */
    @Bean
    public UserServicePort userService(UserPersistencePort userPersistencePort) {
        return new UserServiceAdapter(userPersistencePort);
    }
}
