package io.cmartinezs.authboot.core.entity.domain.user;

import io.cmartinezs.authboot.core.entity.domain.CommonValidations;
import io.cmartinezs.authboot.core.entity.domain.DomainBase;
import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** This class represents a user domain object. */
@Getter
@RequiredArgsConstructor
public class User extends DomainBase implements UserValidations, CommonValidations {
  private static final String ROLE_PREFIX = "ROLE_";
  private final String username;
  private final String email;
  private final String password;
  private final Set<String> authorities;
  private final LocalDateTime passwordResetAt;

  public User(UserPersistence userPersistence) {
    this.username = userPersistence.getUsername();
    this.email = userPersistence.getEmail();
    this.password = userPersistence.getPassword();
    this.authorities =
        userPersistence.getRoles().stream()
            .map(RolePersistence::getFunctions)
            .flatMap(Collection::stream)
            .map(User::toAuthority)
            .collect(Collectors.toSet());
    this.createdAt = userPersistence.getCreatedAt();
    this.updatedAt = userPersistence.getUpdatedAt();
    this.expiredAt = userPersistence.getExpiredAt();
    this.lockedAt = userPersistence.getLockedAt();
    this.enabledAt = userPersistence.getEnabledAt();
    this.disabledAt = userPersistence.getDisabledAt();
    this.passwordResetAt = userPersistence.getPasswordResetAt();
  }

  private static String toAuthority(FunctionPersistence fp) {
    return String.format("%s%s_%s", ROLE_PREFIX, fp.getCode(), fp.getType());
  }

  @Override
  public boolean isExpired() {
    return expiredAt != null && expiredAt.isBefore(LocalDateTime.now());
  }

  @Override
  public boolean isLocked() {
    return lockedAt != null && lockedAt.isBefore(LocalDateTime.now());
  }

  @Override
  public boolean isEnabled() {
    return enabledAt != null && enabledAt.isBefore(LocalDateTime.now());
  }

  @Override
  public boolean mustChangePassword() {
    return passwordResetAt != null && passwordResetAt.isBefore(LocalDateTime.now());
  }
}
