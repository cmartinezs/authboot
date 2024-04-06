package io.cmartinezs.authboot.api.response.auth;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class represents a response to a login request. It contains the username, email, authorities
 * and a token.
 */
@Getter
@RequiredArgsConstructor
public class AuthLoginSuccessResponse {
  private final String username;
  private final String email;
  private final Set<String> authorities;
  private final String token;
}
