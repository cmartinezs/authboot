package io.cmartinezs.authboot.infra.persistence.adapter.jpa;

import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.infra.persistence.entity.jpa.auth.Assignment;
import io.cmartinezs.authboot.infra.persistence.entity.jpa.auth.FunctionType;
import io.cmartinezs.authboot.infra.persistence.entity.jpa.auth.Permission;
import io.cmartinezs.authboot.infra.persistence.entity.jpa.auth.Role;
import io.cmartinezs.authboot.infra.persistence.entity.jpa.auth.User;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

/**
 * @author Carlos
 * @version 1.0
 */
@UtilityClass
public class AuthJpaAdapterUtils {

  static Function<Assignment, RolePersistence> toRolePersistence() {
    return assignment -> {
      Role role = assignment.getRole();
      return new RolePersistence(
          role.getCode(), role.getName(), toFunctionPersistence(role.getPermissions()));
    };
  }

  private static Set<FunctionPersistence> toFunctionPersistence(Set<Permission> permissions) {
    return permissions.stream().map(toFunctionPersistence()).collect(Collectors.toSet());
  }

  private static Function<Permission, FunctionPersistence> toFunctionPersistence() {
    return permission -> {
      io.cmartinezs.authboot.infra.persistence.entity.jpa.auth.Function function =
          permission.getFunction();
      FunctionType functionType = permission.getType();
      return new FunctionPersistence(
          function.getCode(), function.getName(), functionType.getCode(), functionType.getName());
    };
  }

  static Function<User, UserPersistence> toUserPersistence(Set<RolePersistence> roles) {
    return user -> {
      var userPersistence =
          new UserPersistence(user.getUsername(), user.getEmail(), user.getPassword(), roles);
      userPersistence.setExpiredAt(user.getExpiredAt());
      userPersistence.setEnabledAt(user.getEnabledAt());
      userPersistence.setLockedAt(user.getLockedAt());
      userPersistence.setPasswordResetAt(user.getPasswordResetAt());
      return userPersistence;
    };
  }
}
