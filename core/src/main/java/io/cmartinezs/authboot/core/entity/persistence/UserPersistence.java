package io.cmartinezs.authboot.core.entity.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a user persistence object.
 */
@Getter
@RequiredArgsConstructor
public class UserPersistence implements PersistenceBase  {
  private final String username;
  private final String email;
  private final String password;
  private final Set<RolePersistence> roles;
  @Setter private LocalDateTime enabledAt;
  @Setter private LocalDateTime expiredAt;
  @Setter private LocalDateTime lockedAt;
  @Setter private LocalDateTime passwordResetAt;

  /**
   * This method returns the role codes of the user.
   * @return a set of role codes.
   */
  public Set<String> getRoleCodes() {
    return getRoles().stream().map(RolePersistence::code).collect(Collectors.toSet());
  }
}
