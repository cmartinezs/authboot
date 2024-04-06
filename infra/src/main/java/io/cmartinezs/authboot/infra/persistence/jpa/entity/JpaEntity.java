package io.cmartinezs.authboot.infra.persistence.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * This class is the base class for all JPA entities.
 * <p>
 * This class is responsible for defining the common fields for all JPA entities.
 * <p>
 * The common fields are:
 * <p>
 * - id: The id of the entity.
 * - createdAt: The date when the entity was created.
 * - updatedAt: The date when the entity was updated.
 * - enabledAt: The date when the entity was enabled.
 * - disabledAt: The date when the entity was disabled.
 * - expiredAt: The date when the entity was expired.
 * - lockedAt: The date when the entity was locked.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class JpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Integer id;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "enabled_at")
  private LocalDateTime enabledAt;

  @Column(name = "disabled_at")
  private LocalDateTime disabledAt;

  @Column(name = "expired_at")
  private LocalDateTime expiredAt;

  @Column(name = "locked_at")
  private LocalDateTime lockedAt;

  @PrePersist
  public void setCreatedAt() {
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  public void setUpdatedAt() {
    this.updatedAt = LocalDateTime.now();
  }
}
