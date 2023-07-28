package io.cmartinezs.authboot.core.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * This class represents a command to generate a JWT.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class JwtGenerateCmd {
    private final String username;
    private final Set<String> authorities;
}
