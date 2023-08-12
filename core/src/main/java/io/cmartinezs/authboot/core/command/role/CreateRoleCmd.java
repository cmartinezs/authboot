package io.cmartinezs.authboot.core.command.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CreateRoleCmd {
    private final String code;
    private final String name;
    private final String description;
    private final Map<String, List<String>> permissions;
}
