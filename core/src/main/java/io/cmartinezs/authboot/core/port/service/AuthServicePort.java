package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.auth.LoginCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;

/** This interface is used to define the authentication service. */
public interface AuthServicePort {
  /**
   * This method is used to authenticate a user.
   *
   * @param loginCmd The command to authenticate a user.
   * @return The user.
   */
  User authenticate(LoginCmd loginCmd);
}
