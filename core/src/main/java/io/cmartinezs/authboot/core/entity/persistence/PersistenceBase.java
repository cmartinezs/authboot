package io.cmartinezs.authboot.core.entity.persistence;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class PersistenceBase {
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime enabledAt;
    protected LocalDateTime disabledAt;
    protected LocalDateTime expiredAt;
    protected LocalDateTime lockedAt;
}
