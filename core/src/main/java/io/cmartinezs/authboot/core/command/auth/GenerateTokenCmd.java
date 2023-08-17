package io.cmartinezs.authboot.core.command.auth;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * This class represents a command to generate a JWT.
 *
 * <p>The command contains a username and a set of authorities. The username and authorities are
 * used to generate the JWT. The JWT is returned in the response.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class GenerateTokenCmd {
  private final String username;
  private final Set<String> authorities;
}
