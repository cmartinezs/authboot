package io.cmartinezs.authboot.core.entity.persistence;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public class UserPersistence {
  private final String username;
  private final String email;
  private final String password;
  private final Set<RolePersistence> roles;
  @Setter private LocalDateTime enabledAt;
  @Setter private LocalDateTime expiredAt;
  @Setter private LocalDateTime lockedAt;
  @Setter private LocalDateTime passwordResetAt;
}
