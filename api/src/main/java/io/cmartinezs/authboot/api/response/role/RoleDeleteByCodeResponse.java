package io.cmartinezs.authboot.api.response.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleDeleteByCodeResponse {
  private final RoleResponse role;
}
