package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.user.EmailValidationCmd;
import io.cmartinezs.authboot.core.command.user.PasswordRecoveryEmailCmd;

public interface EmailServicePort {

  void sendPasswordRecovery(PasswordRecoveryEmailCmd cmd);

  void sendValidation(EmailValidationCmd cmd);
}
