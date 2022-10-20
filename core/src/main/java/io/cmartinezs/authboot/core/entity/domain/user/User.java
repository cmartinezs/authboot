package io.cmartinezs.authboot.core.entity.domain.user;

import io.cmartinezs.authboot.core.entity.domain.CommonValidations;
import io.cmartinezs.authboot.core.entity.persistence.FunctionPersistence;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Carlos
 * @version 1.0
 */
public record User(UserPersistence userPersistence) implements UserValidations, CommonValidations {

  private static final String ROLE_PREFIX = "ROLE_";

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
    return userPersistence.getRoles().stream()
        .map(toAuthorities())
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }

  private static Function<RolePersistence, Set<String>> toAuthorities() {
    return rp -> rp.functions().stream().map(toAuthority(rp)).collect(Collectors.toSet());
  }

  private static Function<FunctionPersistence, String> toAuthority(RolePersistence rp) {
    return fp -> String.format("%s_%s_%s_%s", ROLE_PREFIX, rp.code(), fp.code(), fp.type());
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
