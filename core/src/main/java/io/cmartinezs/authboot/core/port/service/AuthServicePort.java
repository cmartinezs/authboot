package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.LoginCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;

/**
 * @author Carlos
 * @version 1.0
 */
public interface AuthServicePort {
  User authenticate(LoginCmd loginCmd);
}
