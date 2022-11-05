package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.JwtGenerateCmd;

/**
 * @author Carlos
 * @version 1.0
 */
public interface TokenServicePort {
  String generate(JwtGenerateCmd jwtGenerateCmd);
}
