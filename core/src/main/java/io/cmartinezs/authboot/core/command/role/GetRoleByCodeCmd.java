package io.cmartinezs.authboot.core.command.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRoleByCodeCmd {
  private final String code;
}
