package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.user.*;
import io.cmartinezs.authboot.core.entity.domain.user.User;

/**
 * @author Carlos
 * @version 1.0
 */
public interface UserServicePort {
  User getUserByUsername(String username);

  User getUserByEmail(String email);

  void recoverPassword(RecoverPasswordCmd cmd);

  Integer createUser(CreateUserCmd cmd);

  void validateUser(ValidateUserCmd cmd);

  User getUser(GetUserCmd cmd);

  void processPasswordRecoveryRequest(PasswordRecoveryRequestCmd email);

  User deleteUser(DeleteUserCmd cmd);

  User updateUser(UpdateUserCmd cmd);
}
