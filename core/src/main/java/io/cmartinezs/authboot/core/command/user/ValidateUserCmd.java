package io.cmartinezs.authboot.core.command.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidateUserCmd {
    private final String username;
    private final String email;
    private final String validationCode;
}
