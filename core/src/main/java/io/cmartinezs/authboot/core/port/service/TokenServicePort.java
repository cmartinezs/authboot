package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.auth.GenerateTokenCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import java.util.Optional;

/** This interface is used to define the token service. */
public interface TokenServicePort {

  /**
   * This method is used to generate a token.
   *
   * @param generateTokenCmd The command to generate a token.
   * @return The token.
   */
  String generate(GenerateTokenCmd generateTokenCmd);

  Optional<String> getUsername(String authToken);

  boolean validate(String authToken, User user);
}
