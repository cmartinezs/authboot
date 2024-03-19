package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.user.PasswordRecoveryEmailCmd;
import io.cmartinezs.authboot.core.command.user.EmailValidationCmd;

public interface EmailServicePort {

  void sendPasswordRecovery(PasswordRecoveryEmailCmd cmd);

  void sendValidation(EmailValidationCmd cmd);
}
