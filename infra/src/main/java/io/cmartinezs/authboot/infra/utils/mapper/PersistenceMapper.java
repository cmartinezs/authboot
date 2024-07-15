package io.cmartinezs.authboot.infra.utils.mapper;

import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.AssignmentEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.PermissionEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.RoleEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.UserEntity;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

/** This class is used to map entities to persistence objects. */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PersistenceMapper {

  /**
   * This method maps a user persistence object to a user entity.
   *
   * @param userPersistence The user persistence object to be mapped.
   * @return The user entity.
   */
  public static UserEntity persistenceToEntity(UserPersistence userPersistence) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userPersistence.getUsername());
    userEntity.setEmail(userPersistence.getEmail());
    userEntity.setPassword(userPersistence.getPassword());
    userEntity.setPasswordResetAt(userPersistence.getPasswordResetAt());
    userEntity.setValidationToken(userPersistence.getValidationCode());
    userEntity.setValidationTokenExpiredAt(userPersistence.getValidationCodeExpiredAt());
    userEntity.setEnabledAt(userPersistence.getEnabledAt());
    return userEntity;
  }

  /**
   * This method maps a user entity to a user persistence object.
   *
   * @param user The user entity to be mapped.
   * @return The user persistence object.
   */
  public static UserPersistence entityToPersistence(UserEntity user) {
    var roles = assignmentsToRoles(user.getAssignments());
    var userPersistence =
        new UserPersistence(user.getUsername(), user.getEmail(), user.getPassword(), roles, user.getPasswordResetAt());
    userPersistence.setCreatedAt(user.getCreatedAt());
    userPersistence.setUpdatedAt(user.getUpdatedAt());
    userPersistence.setExpiredAt(user.getExpiredAt());
    userPersistence.setEnabledAt(user.getEnabledAt());
    userPersistence.setDisabledAt(user.getDisabledAt());
    userPersistence.setLockedAt(user.getLockedAt());
    userPersistence.setPasswordResetAt(user.getPasswordResetAt());
    return userPersistence;
  }

  /**
   * This method maps a set of assignments to a set of roles.
   *
   * @param assignments The set of assignments to be mapped.
   * @return The set of roles.
   */
  public static Set<RolePersistence> assignmentsToRoles(Set<AssignmentEntity> assignments) {
    return assignments.stream()
        .map(AssignmentEntity::getRole)
        .map(PersistenceMapper::entityToPersistence)
        .collect(Collectors.toSet());
  }

  /**
   * This method maps a role entity to a role persistence object.
   *
   * @param roleEntity The role entity to be mapped.
   * @return The role persistence object.
   */
  public static RolePersistence entityToPersistence(RoleEntity roleEntity) {
    var rolePersistence =
        new RolePersistence(
            roleEntity.getCode(),
            roleEntity.getName(),
            roleEntity.getDescription(),
            entitiesToPersistence(roleEntity.getPermissions()));
    rolePersistence.setCreatedAt(roleEntity.getCreatedAt());
    rolePersistence.setUpdatedAt(roleEntity.getUpdatedAt());
    rolePersistence.setEnabledAt(roleEntity.getEnabledAt());
    rolePersistence.setDisabledAt(roleEntity.getDisabledAt());
    rolePersistence.setExpiredAt(roleEntity.getExpiredAt());
    rolePersistence.setLockedAt(roleEntity.getLockedAt());
    return rolePersistence;
  }

  /**
   * This method maps a set of permission entities to a set of function persistence objects.
   *
   * @param permissions The set of permission entities to be mapped.
   * @return The set of function persistence objects.
   */
  public static Set<FunctionPersistence> entitiesToPersistence(Set<PermissionEntity> permissions) {
    return permissions.stream()
        .map(PersistenceMapper::permissionToFunction)
        .collect(Collectors.toSet());
  }

  /**
   * This method maps a permission entity to a function persistence object.
   *
   * @param permissionEntity The permission entity to be mapped.
   * @return The function persistence object.
   */
  public static FunctionPersistence permissionToFunction(PermissionEntity permissionEntity) {
    var function = permissionEntity.getFunction();
    var functionType = permissionEntity.getType();
    return new FunctionPersistence(
        function.getCode(), function.getName(), functionType.getCode(), functionType.getName());
  }

  /**
   * This method maps a list of role entities to a set of role persistence objects.
   *
   * @param all The list of role entities to be mapped.
   * @return The set of role persistence objects.
   */
  public static Set<RolePersistence> rolesToRolePersistence(List<RoleEntity> all) {
    return all.stream().map(PersistenceMapper::entityToPersistence).collect(Collectors.toSet());
  }

  /**
   * This method maps a role persistence object to a role entity.
   *
   * @param rolePersistence The role persistence object to be mapped.
   * @return The role entity.
   */
  public static RoleEntity persistenceToEntity(RolePersistence rolePersistence) {
    var roleEntity = new RoleEntity();
    roleEntity.setCode(rolePersistence.getCode());
    roleEntity.setName(rolePersistence.getName());
    roleEntity.setDescription(rolePersistence.getDescription());
    return roleEntity;
  }

  public static Set<FunctionPersistence> permissionsToFunctionPersistence(
      Set<PermissionEntity> permissions) {
    return permissions.stream()
        .map(PersistenceMapper::permissionToFunction)
        .collect(Collectors.toSet());
  }

  public static Set<RolePersistence> entityToAssigmentPersistence(
      Set<AssignmentEntity> assignments) {
    return assignments.stream()
        .map(AssignmentEntity::getRole)
        .map(PersistenceMapper::entityToPersistence)
        .collect(Collectors.toSet());
  }
}
