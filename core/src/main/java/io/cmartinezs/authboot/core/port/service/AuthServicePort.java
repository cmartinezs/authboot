package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.LoginCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;

import java.util.Optional;

/**
 * This interface is used to define the authentication service.
 */
public interface AuthServicePort {
  /**
   * This method is used to authenticate a user.
   * @param loginCmd The command to authenticate a user.
   * @return The user.
   */
  User authenticate(LoginCmd loginCmd);
  /**
   * This method is used to encrypt a password.
   * @param password The password to encrypt.
   * @return The encrypted password.
   */
  String encrypt(String password);

    Optional<User> getByUsername(String username);
}
