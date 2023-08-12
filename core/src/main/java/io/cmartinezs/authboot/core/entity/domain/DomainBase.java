package io.cmartinezs.authboot.core.entity.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
