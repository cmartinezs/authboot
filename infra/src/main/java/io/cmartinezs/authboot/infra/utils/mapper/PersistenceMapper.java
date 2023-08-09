package io.cmartinezs.authboot.infra.utils.mapper;

import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.AssignmentEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.PermissionEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.RoleEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.UserEntity;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is used to map entities to persistence objects.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PersistenceMapper {
    /**
     * This method maps a user entity to a user persistence object.
     *
     * @param userEntity The user entity to be mapped.
     * @return The user persistence object.
     */
    public static UserPersistence entityToPersistence(UserEntity userEntity) {
        var up = new UserPersistence(userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword(), assignmentsToRoles(userEntity.getAssignments()));
        up.setEnabledAt(userEntity.getEnabledAt());
        up.setExpiredAt(userEntity.getExpiredAt());
        up.setLockedAt(userEntity.getLockedAt());
        up.setPasswordResetAt(userEntity.getPasswordResetAt());
        return up;
    }

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
        userEntity.setExpiredAt(userPersistence.getExpiredAt());
        userEntity.setEnabledAt(userPersistence.getEnabledAt());
        userEntity.setLockedAt(userPersistence.getLockedAt());
        userEntity.setPasswordResetAt(userPersistence.getPasswordResetAt());
        return userEntity;
    }

    /**
     * This method maps a user entity to a user persistence object.
     *
     * @param user The user entity to be mapped.
     * @return The user persistence object.
     */
    public static UserPersistence persistenceToEntity(UserEntity user) {
        var roles = assignmentsToRoles(user.getAssignments());
        var userPersistence =
                new UserPersistence(user.getUsername(), user.getEmail(), user.getPassword(), roles);
        userPersistence.setExpiredAt(user.getExpiredAt());
        userPersistence.setEnabledAt(user.getEnabledAt());
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
        return assignments
                .stream()
                .map(AssignmentEntity::getRole)
                .map(PersistenceMapper::entityToPersistence)
                .collect(Collectors.toSet());
    }

    public static Set<RolePersistence> assignmentsToRoles(List<AssignmentEntity> assignments) {
        return assignments
                .stream()
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
        return new RolePersistence(roleEntity.getCode(), roleEntity.getName(), entitiesToPersistence(roleEntity.getPermissions()));
    }

    /**
     * This method maps a set of permission entities to a set of function persistence objects.
     *
     * @param permissions The set of permission entities to be mapped.
     * @return The set of function persistence objects.
     */
    public static Set<FunctionPersistence> entitiesToPersistence(Set<PermissionEntity> permissions) {
        return permissions
                .stream()
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
        return new FunctionPersistence(function.getCode(), function.getName(), functionType.getCode(), functionType.getName());
    }
}
