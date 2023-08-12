package io.cmartinezs.authboot.core.adapter.service;

import io.cmartinezs.authboot.core.command.role.CreateRoleCmd;
import io.cmartinezs.authboot.core.command.role.GetRoleByCodeCmd;
import io.cmartinezs.authboot.core.entity.domain.user.Function;
import io.cmartinezs.authboot.core.entity.domain.user.Role;
import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.exception.persistence.ExistsEntityException;
import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.core.port.service.RoleServicePort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RoleServiceAdapter implements RoleServicePort {
    private final RolePersistencePort rolePersistencePort;

    private static Set<FunctionPersistence> toFunctions(Map.Entry<String, List<String>> permission) {
        return permission.getValue()
                .stream()
                .map(type -> new FunctionPersistence(permission.getKey(), null, type, null))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Role> getAll() {
        return toRoles(rolePersistencePort.findAll());
    }

    @Override
    public Role getByCode(GetRoleByCodeCmd cmd) {
        var code = cmd.getCode();
        RolePersistence role = rolePersistencePort.findByCode(code)
                .orElseThrow(() -> new NotFoundEntityException("role", "code", code));
        return toRoleFull(role);
    }

    @Override
    public Integer createRole(CreateRoleCmd cmd) {
        var code = cmd.getCode();
        if (rolePersistencePort.findByCode(code).isPresent()) {
            throw new ExistsEntityException(RolePersistence.class, "code", code);
        }
        return rolePersistencePort.save(toRolePersistence(cmd));
    }

    private RolePersistence toRolePersistence(CreateRoleCmd cmd) {
        return new RolePersistence(cmd.getCode(), cmd.getName(), cmd.getDescription(), toFunctionPersistence(cmd.getPermissions()));
    }

    private Set<FunctionPersistence> toFunctionPersistence(Map<String, List<String>> permissions) {
        return permissions.entrySet()
                .stream()
                .map(RoleServiceAdapter::toFunctions)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Role toRoleFull(RolePersistence rolePersistence) {
        Role role = toRole(rolePersistence);
        role.setCreatedAt(rolePersistence.getCreatedAt());
        role.setUpdatedAt(rolePersistence.getUpdatedAt());
        role.setEnabledAt(rolePersistence.getEnabledAt());
        role.setDisabledAt(rolePersistence.getDisabledAt());
        role.setExpiredAt(rolePersistence.getExpiredAt());
        role.setLockedAt(rolePersistence.getLockedAt());
        return role;
    }

    private Role toRole(RolePersistence rolePersistence) {
        return new Role(rolePersistence.getCode(), rolePersistence.getName(), toFunctions(rolePersistence.getFunctions()));
    }

    private Set<Role> toRoles(Set<RolePersistence> roles) {
        return roles.stream()
                .map(this::toRole)
                .collect(Collectors.toSet());
    }

    private Set<Function> toFunctions(Set<FunctionPersistence> functions) {
        return functions.stream()
                .map(function -> new Function(function.getCode() + "_" + function.getType(), function.getName() + " " + function.getTypeName()))
                .collect(Collectors.toSet());
    }
}
