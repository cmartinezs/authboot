package io.cmartinezs.authboot.api.response.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RolePatchByCodeResponse {
  private final RoleResponse role;
}
