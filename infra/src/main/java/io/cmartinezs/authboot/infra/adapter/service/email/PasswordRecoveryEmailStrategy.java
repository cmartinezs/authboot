package io.cmartinezs.authboot.infra.adapter.service.email;

import io.cmartinezs.authboot.core.command.user.PasswordRecoveryEmailCmd;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordRecoveryEmailStrategy implements EmailStrategy {
  private final PasswordRecoveryEmailCmd cmd;

  @Override
  public String getConfigName() {
    return "password-recovery";
  }

  @Override
  public Map<String, String> getVariables() {
    return Map.of("email", cmd.getEmail(), "username", cmd.getUsername(), "validationCode", cmd.getValidationCode());
  }

  @Override
  public Map<String,  Map<String, Map<String, String>>> getUris() {
    return Map.of(
            "recovery-link", Map.of("path-variables", Map.of("validationCode", cmd.getValidationCode())));
  }
}
