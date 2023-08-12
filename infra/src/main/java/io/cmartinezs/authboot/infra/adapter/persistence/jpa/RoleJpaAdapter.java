package io.cmartinezs.authboot.infra.adapter.persistence.jpa;

import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.FunctionEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.FunctionTypeEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.PermissionEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.RoleEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.FunctionRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.FunctionTypeRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.PermissionRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.RoleRepository;
import io.cmartinezs.authboot.infra.utils.mapper.PersistenceMapper;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * This class is used to define the role persistence port.
 */
@RequiredArgsConstructor
public class RoleJpaAdapter implements RolePersistencePort {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final FunctionRepository functionRepository;
    private final FunctionTypeRepository functionTypeRepository;

    private static void validateBeforeSave(Set<String> functionsCodes, Map<String, FunctionEntity> functionsByCode, Set<String> functionTypeCodes, Map<String, FunctionTypeEntity> functionTypeByCode) {
        var missingFunctionCodes = new HashSet<>(functionsCodes);
        missingFunctionCodes.removeAll(functionsByCode.keySet());

        var missingFunctionTypeCodes = new HashSet<>(functionTypeCodes);
        missingFunctionTypeCodes.removeAll(functionTypeByCode.keySet());

        Set<NotFoundEntityException> errors = new HashSet<>();
        missingFunctionCodes.forEach(code -> errors.add(new NotFoundEntityException("function", "code", code)));
        missingFunctionTypeCodes.forEach(code -> errors.add(new NotFoundEntityException("function_type", "code", code)));

        if (!errors.isEmpty()) {
            throw new NotFoundEntityException(errors);
        }
    }

    private static PermissionEntity newPermissionEntity(RoleEntity saved, FunctionEntity functionEntity, FunctionTypeEntity functionTypeEntity) {
        var permissionEntity = new PermissionEntity();
        permissionEntity.setRole(saved);
        permissionEntity.setFunction(functionEntity);
        permissionEntity.setType(functionTypeEntity);
        return permissionEntity;
    }

    /**
     * Finds the set of {@link RolePersistence} in the database.
     *
     * @return The set {@link RolePersistence} in the database.
     */
    @Override
    public Set<RolePersistence> findAll() {
        return PersistenceMapper.rolesToRolePersistence(roleRepository.findAll());
    }

    /**
     * Finds the {@link RolePersistence} with the given code.
     *
     * @param code The code of the role to find.
     * @return The {@link RolePersistence} with the given code.
     */
    @Override
    public Optional<RolePersistence> findByCode(String code) {
        return roleRepository.findByCode(code).map(PersistenceMapper::entityToPersistence);
    }

    @Override
    public Integer save(RolePersistence rolePersistence) {
        var functionsCodes = rolePersistence.getFunctionsCodes();
        var functionsByCode = functionRepository.findByCodeIn(functionsCodes)
                .stream()
                .collect(Collectors.toMap(FunctionEntity::getCode, Function.identity()));
        var functionTypeCodes = rolePersistence.getFunctionTypeCodes();
        var functionTypeByCode = functionTypeRepository.findByCodeIn(functionTypeCodes)
                .stream()
                .collect(Collectors.toMap(FunctionTypeEntity::getCode, Function.identity()));

        validateBeforeSave(functionsCodes, functionsByCode, functionTypeCodes, functionTypeByCode);

        var saved = roleRepository.save(PersistenceMapper.persistenceToEntity(rolePersistence));
        var permissions = rolePersistence.getFunctions()
                .stream()
                .map(function -> newPermissionEntity(saved, functionsByCode.get(function.getCode()), functionTypeByCode.get(function.getType())))
                .collect(Collectors.toSet());

        permissionRepository.saveAll(permissions);
        return saved.getId();
    }
}
