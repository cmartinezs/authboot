package io.cmartinezs.authboot.core.entity.domain.user;

import io.cmartinezs.authboot.core.entity.domain.CommonValidations;
import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a user domain object.
 */
public record User(UserPersistence userPersistence) implements UserValidations, CommonValidations {

  private static final String ROLE_PREFIX = "ROLE_";

  private static String toAuthority(FunctionPersistence fp) {
    return String.format("%s%s_%s", ROLE_PREFIX, fp.code(), fp.type());
  }

  @Override
  public boolean isExpired() {
    var expiredAt = userPersistence.getExpiredAt();
    return expiredAt != null && expiredAt.isBefore(LocalDateTime.now());
  }

  @Override
  public boolean isLocked() {
    var lockedAt = userPersistence.getLockedAt();
    return lockedAt != null && lockedAt.isBefore(LocalDateTime.now());
  }

  @Override
  public boolean isEnabled() {
    var enabledAt = userPersistence.getEnabledAt();
    return enabledAt != null && enabledAt.isBefore(LocalDateTime.now());
  }

  @Override
  public boolean mustChangePassword() {
    var passwordResetAt = this.userPersistence.getPasswordResetAt();
    return passwordResetAt != null && passwordResetAt.isBefore(LocalDateTime.now());
  }

  public Set<String> getAuthorities() {
    return this.userPersistence.getRoles()
        .stream()
        .map(RolePersistence::functions)
        .flatMap(Collection::stream)
        .map(User::toAuthority)
        .collect(Collectors.toSet());
  }

  public String getPassword() {
    return userPersistence.getPassword();
  }

  public String getUsername() {
    return userPersistence.getUsername();
  }

  public String getEmail() {
    return userPersistence.getEmail();
  }
}
