package io.cmartinezs.authboot.api.response;

import io.cmartinezs.authboot.core.entity.domain.user.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleGetByCodeSuccess {
    private final Role role;
}
