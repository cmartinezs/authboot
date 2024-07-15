package io.cmartinezs.authboot.infra.adapter.service.email;

import io.cmartinezs.authboot.core.command.user.EmailValidationCmd;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailValidationStrategy implements EmailStrategy {
  private final EmailValidationCmd cmd;

  @Override
  public String getConfigName() {
    return "email-validation";
  }

  @Override
  public Map<String, String> getVariables() {
    return Map.of(
        "email", cmd.getEmail(),
        "username", cmd.getUsername(),
        "validationCode", cmd.getValidationCode());
  }

  @Override
  public Map<String, Map<String, Map<String, String>>> getUris() {
    return Map.of(
        "validation-link", Map.of("path-variables", Map.of("validationCode", cmd.getValidationCode())));
  }
}
