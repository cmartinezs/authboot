package io.cmartinezs.authboot.core.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * This class represents a command to create a user.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class UpdateUserCmd {
    private final String username;
    private String newPassword;
    private String oldPassword;
    private String email;
    private Set<String> roleCodes;
}
