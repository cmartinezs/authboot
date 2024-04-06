package io.cmartinezs.authboot.core.adapter.service;

import io.cmartinezs.authboot.core.command.role.CreateRoleCmd;
import io.cmartinezs.authboot.core.command.role.GetRoleByCodeCmd;
import io.cmartinezs.authboot.core.command.role.RoleDeleteCmd;
import io.cmartinezs.authboot.core.command.role.RoleUpdateCmd;
import io.cmartinezs.authboot.core.entity.domain.user.Role;
import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.exception.persistence.ExistsEntityException;
import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.core.port.service.RoleServicePort;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleServiceAdapter implements RoleServicePort {
  private final RolePersistencePort rolePersistencePort;

  private static Set<FunctionPersistence> toFunctions(Map.Entry<String, List<String>> permission) {
    return permission.getValue().stream()
        .map(type -> new FunctionPersistence(permission.getKey(), null, type, null))
        .collect(Collectors.toSet());
  }

  private static RolePersistence toRolePersistence(RoleUpdateCmd cmd) {
    return new RolePersistence(
        cmd.getCode(),
        cmd.getName(),
        cmd.getDescription(),
        toFunctionPersistence(cmd.getPermissions()));
  }

  private static RolePersistence toRolePersistence(CreateRoleCmd cmd) {
    return new RolePersistence(
        cmd.getCode(),
        cmd.getName(),
        cmd.getDescription(),
        toFunctionPersistence(cmd.getPermissions()));
  }

  private static Set<FunctionPersistence> toFunctionPersistence(
      Map<String, List<String>> permissions) {
    return permissions.entrySet().stream()
        .map(RoleServiceAdapter::toFunctions)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<Role> getAll() {
    return rolePersistencePort.findAll().stream()
        .map(RolePersistence::toDomain)
        .collect(Collectors.toSet());
  }

  @Override
  public Role getByCode(GetRoleByCodeCmd cmd) {
    var code = cmd.getCode();
    return rolePersistencePort
        .findByCode(code)
        .orElseThrow(() -> new NotFoundEntityException("role", "code", code))
        .toDomain();
  }

  @Override
  public Integer createRole(CreateRoleCmd cmd) {
    var code = cmd.getCode();
    if (rolePersistencePort.findByCode(code).isPresent()) {
      throw new ExistsEntityException("role", "code", code);
    }
    return rolePersistencePort.save(toRolePersistence(cmd));
  }

  @Override
  public Role updateRole(RoleUpdateCmd cmd) {
    var code = cmd.getCode();
    var foundRole =
        rolePersistencePort
            .findByCode(code)
            .orElseThrow(() -> new NotFoundEntityException("role", "code", code));
    return rolePersistencePort.edit(foundRole, toRolePersistence(cmd)).toDomain();
  }

  @Override
  public Role deleteRole(RoleDeleteCmd cmd) {
    var code = cmd.getCode();
    var foundRole =
        rolePersistencePort
            .findByCode(code)
            .orElseThrow(() -> new NotFoundEntityException("role", "code", code));
    rolePersistencePort.delete(foundRole);
    return foundRole.toDomain();
  }
}
