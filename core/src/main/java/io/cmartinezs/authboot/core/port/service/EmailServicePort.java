package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.user.SendPasswordRecoveryCmd;

public interface EmailServicePort {
    void sendPasswordRecovery(SendPasswordRecoveryCmd cmd);
}
