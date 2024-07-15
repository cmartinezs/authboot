package io.cmartinezs.authboot.infra.adapter.persistence.jpa;

import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
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
import io.cmartinezs.authboot.infra.utils.InfraCollections;
import io.cmartinezs.authboot.infra.utils.mapper.PersistenceMapper;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/** This class is used to define the role persistence port. */
@RequiredArgsConstructor
public class RoleJpaAdapter implements RolePersistencePort {
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final FunctionRepository functionRepository;
  private final FunctionTypeRepository functionTypeRepository;

  /**
   * @param role
   * @param function
   * @param type
   * @return
   */
  private static PermissionEntity newPermissionEntity(
      RoleEntity role, FunctionEntity function, FunctionTypeEntity type) {
    var permissionEntity = new PermissionEntity();
    permissionEntity.setRole(role);
    permissionEntity.setFunction(function);
    permissionEntity.setType(type);
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
  @Transactional
  public Integer save(RolePersistence rolePersistence) {
    var functionAndTypeMap = mapFunctionsAndTypes(rolePersistence);
    var saved = roleRepository.save(PersistenceMapper.persistenceToEntity(rolePersistence));
    createPermissions(saved, rolePersistence.getFunctions(), functionAndTypeMap);
    return saved.getId();
  }

  @Override
  @Transactional
  public RolePersistence edit(RolePersistence actualRole, RolePersistence newRole) {
    var editedRole = actualRole.merge(newRole);
    var functionAndTypeMap = mapFunctionsAndTypes(editedRole);
    var foundRoleEntity = edit(editedRole);
    var permissions =
        createPermissions(foundRoleEntity, editedRole.getFunctions(), functionAndTypeMap);
    editedRole.setFunctions(PersistenceMapper.permissionsToFunctionPersistence(permissions));
    return editedRole;
  }

  @Override
  @Transactional
  public void delete(RolePersistence foundRole) {
    var foundRoleEntity =
        roleRepository
            .findByCode(foundRole.getCode())
            .orElseThrow(() -> createNotFoundEntityException(foundRole));
    var permissions = permissionRepository.findByRoleCode(foundRole.getCode());
    permissionRepository.deleteAll(permissions);
    roleRepository.delete(foundRoleEntity);
  }

  private static NotFoundEntityException createNotFoundEntityException(RolePersistence foundRole) {
    return new NotFoundEntityException("role", "code", foundRole.getCode());
  }

  private RoleEntity edit(RolePersistence editedRole) {
    var foundRoleEntity =
        roleRepository
            .findByCode(editedRole.getCode())
            .orElseThrow(() -> createNotFoundEntityException(editedRole));
    foundRoleEntity.setName(editedRole.getName());
    foundRoleEntity.setDescription(editedRole.getDescription());
    roleRepository.save(foundRoleEntity);
    return foundRoleEntity;
  }

  private Set<PermissionEntity> createPermissions(
      RoleEntity foundRoleEntity,
      Set<FunctionPersistence> functions,
      FunctionAndTypeMap functionAndTypeMap) {
    // 1. Obtener los permisos actuales de foundRoleEntity
    var currentPermissions = foundRoleEntity.getPermissions();

    // 2. Identificar los permisos que se van a crear basados en functions
    var newPermissions =
        functions.stream()
            .map(
                function -> {
                  var functionEntity = functionAndTypeMap.functionsByCode.get(function.getCode());
                  var functionTypeEntity =
                      functionAndTypeMap.functionTypeByCode.get(function.getType());
                  return newPermissionEntity(foundRoleEntity, functionEntity, functionTypeEntity);
                })
            .collect(Collectors.toSet());

    // 3. Determinar qué permisos se deben eliminar y cuáles se deben agregar
    var permissionsToDelete = new HashSet<>(currentPermissions);
    permissionsToDelete.removeAll(newPermissions);

    var permissionsToAdd = new HashSet<>(newPermissions);
    permissionsToAdd.removeAll(currentPermissions);

    // 4. Eliminar y agregar los permisos según sea necesario
    if (!permissionsToDelete.isEmpty()) {
      permissionRepository.deleteAll(permissionsToDelete);
      foundRoleEntity.getPermissions().removeAll(permissionsToDelete);
    }

    if (!permissionsToAdd.isEmpty()) {
      permissionRepository.saveAll(permissionsToAdd);
      foundRoleEntity.getPermissions().addAll(permissionsToAdd);
    }

    roleRepository.save(foundRoleEntity);
    return newPermissions;
  }

  private FunctionAndTypeMap mapFunctionsAndTypes(RolePersistence editedRole) {
    Set<NotFoundEntityException> errors = new HashSet<>();

    Map<String, FunctionEntity> functionsByCode =
        InfraCollections.createMapFrom(
            editedRole.getFunctionsCodes(),
            functionRepository::findByCodeIn,
            FunctionEntity::getCode);

    InfraCollections.validateMissingCodes(
            functionsByCode, editedRole.getFunctionsCodes(), "function")
        .ifPresent(errors::add);

    Map<String, FunctionTypeEntity> functionTypeByCode =
        InfraCollections.createMapFrom(
            editedRole.getFunctionTypeCodes(),
            functionTypeRepository::findByCodeIn,
            FunctionTypeEntity::getCode);

    InfraCollections.validateMissingCodes(
            functionTypeByCode, editedRole.getFunctionTypeCodes(), "function_type")
        .ifPresent(errors::add);

    if (!errors.isEmpty()) {
      throw new NotFoundEntityException(errors);
    }

    return new FunctionAndTypeMap(functionsByCode, functionTypeByCode);
  }

  private record FunctionAndTypeMap(
      Map<String, FunctionEntity> functionsByCode,
      Map<String, FunctionTypeEntity> functionTypeByCode) {}
}
