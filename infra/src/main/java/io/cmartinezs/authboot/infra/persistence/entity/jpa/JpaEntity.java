package io.cmartinezs.authboot.infra.persistence.entity.jpa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author carlos
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
