package io.cmartinezs.authboot.infra.persistence.jpa.entity.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.JpaEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class is the JPA entity for the roles table.
 *
 * <p>This class is responsible for defining the roles table.
 *
 * <p>The roles table is defined with the following columns:
 *
 * <p>- code: The code of the role. - name: The name of the role. - description: The description of
 * the role.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends JpaEntity {

  @Column(name = "username", length = 30, nullable = false)
  private String username;

  @Column(name = "password", length = 100, nullable = false)
  private String password;

  @Column(name = "password_reset_at")
  private LocalDateTime passwordResetAt;

  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String email;

  @Column(name = "password_recovery_token", length = 100)
  private String passwordRecoveryToken;

  @Column(name = "password_recovery_expired_at")
  private LocalDateTime passwordRecoveryTokenExpiredAt;

  @Column(name = "validation_token", length = 100)
  private String validationToken;

  @Column(name = "validation_token_expired_at")
  private LocalDateTime validationTokenExpiredAt;

  @Builder.Default
  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private Set<AssignmentEntity> assignments = new HashSet<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private Set<LastPasswordEntity> lastPasswords = new HashSet<>();
}
