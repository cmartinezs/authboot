package io.cmartinezs.authboot.core.command.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecoverPasswordCmd {
    private final String username;
    private final String recoveryCode;
    private final String password;
}
