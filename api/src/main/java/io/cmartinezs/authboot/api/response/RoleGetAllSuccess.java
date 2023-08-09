package io.cmartinezs.authboot.api.response;

import io.cmartinezs.authboot.core.entity.domain.user.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class RoleGetAllSuccess {
    private final Set<Role> roles;
}
