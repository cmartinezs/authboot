package io.cmartinezs.authboot.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class represents a response for the encrypt password endpoint.
 * <p>
 * This class is used to return the encrypted password.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public class JwtEncryptPasswordSuccess {
    private final String password;
    private final String encryptPassword;
}
