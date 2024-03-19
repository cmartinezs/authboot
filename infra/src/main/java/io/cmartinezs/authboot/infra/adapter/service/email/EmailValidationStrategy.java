package io.cmartinezs.authboot.infra.adapter.service.email;

import java.util.Map;

import io.cmartinezs.authboot.core.command.user.EmailValidationCmd;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailValidationStrategy implements EmailStrategy {
    private final EmailValidationCmd cmd;

    @Override
    public String getTemplateName() {
        return "email-validation";
    }

    @Override
    public Map<String, String> getVariables() {
        return null;
    }

    @Override
    public Map<String,  Map<String, Map<String, String>>> getUris() {
        return null;
    }
}
