package io.cmartinezs.authboot.api.response.role;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleGetAllSuccessResponse {
    private final Set<RoleResponse> roles;
}
