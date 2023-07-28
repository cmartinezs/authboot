package io.cmartinezs.authboot.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public class JwtLoginSuccess {
  private final String username;
  private final Set<String> roles;
  private final String token;
}
