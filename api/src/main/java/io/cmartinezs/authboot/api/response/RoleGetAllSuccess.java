package io.cmartinezs.authboot.api.response;

import io.cmartinezs.authboot.core.entity.domain.user.Role;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleGetAllSuccess {
    private final Set<Role> roles;
}
