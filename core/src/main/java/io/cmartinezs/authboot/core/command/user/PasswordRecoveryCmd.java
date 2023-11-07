package io.cmartinezs.authboot.core.command.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordRecoveryCmd {
    private String email;
}
