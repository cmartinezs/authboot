package io.cmartinezs.authboot.api.response.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class represents a response for the encrypt password endpoint.
 *
 * <p>This class is used to return the encrypted password.
 */
@Getter
@RequiredArgsConstructor
public class AuthEncryptPasswordSuccessResponse {
  private final String password;
  private final String encryptPassword;
}
