package io.cmartinezs.authboot.core.entity.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DomainBase {
  protected LocalDateTime createdAt;
  protected LocalDateTime updatedAt;
  protected LocalDateTime expiredAt;
  protected LocalDateTime lockedAt;
  protected LocalDateTime enabledAt;
  protected LocalDateTime disabledAt;
}
