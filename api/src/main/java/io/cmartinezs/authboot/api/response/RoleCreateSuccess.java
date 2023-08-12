package io.cmartinezs.authboot.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleCreateSuccess {
    private final Integer roleId;
}
