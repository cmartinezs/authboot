package io.cmartinezs.authboot.core.command.role;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RoleUpdateCmd {
  private final String code;
  private String name;
  private String description;
  private Map<String, List<String>> permissions;
}
