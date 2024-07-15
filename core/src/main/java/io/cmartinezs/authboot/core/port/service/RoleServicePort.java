package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.role.CreateRoleCmd;
import io.cmartinezs.authboot.core.command.role.RoleDeleteCmd;
import io.cmartinezs.authboot.core.command.role.GetRoleByCodeCmd;
import io.cmartinezs.authboot.core.command.role.RoleUpdateCmd;
import io.cmartinezs.authboot.core.entity.domain.user.Role;
import java.util.Set;

public interface RoleServicePort {
  Set<Role> getAll();

  Role getByCode(GetRoleByCodeCmd cmd);

  Integer createRole(CreateRoleCmd cmd);

  Role updateRole(RoleUpdateCmd cmd);

  Role deleteRole(RoleDeleteCmd cmd);
}
