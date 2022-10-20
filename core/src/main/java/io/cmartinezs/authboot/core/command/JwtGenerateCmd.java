package io.cmartinezs.authboot.core.command;

import java.util.Set;

/**
 * @author Carlos
 * @version 1.0
 */
public record JwtGenerateCmd(String username, Set<String> authorities) {}
