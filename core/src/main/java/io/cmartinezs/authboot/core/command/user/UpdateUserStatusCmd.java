package io.cmartinezs.authboot.core.command.user;

import io.cmartinezs.authboot.core.entity.domain.user.UserStatus;
import lombok.Getter;

@Getter
public class UpdateUserStatusCmd {
    private final String username;
    private final UserStatus userStatus;

    public UpdateUserStatusCmd(String username, String status) {
        this.username = username;
        this.userStatus = UserStatus.valueOf(status.toUpperCase());
    }
}
