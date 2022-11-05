package io.cmartinezs.authboot.infra.persistence.entity.jpa.auth;

import io.cmartinezs.authboot.infra.persistence.entity.jpa.JpaEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends JpaEntity {

  @Column(name = "username", length = 30, nullable = false)
  private String username;

  @Column(name = "password", length = 100, nullable = false)
  private String password;

  @Column(name = "password_reset_at")
  private LocalDateTime passwordResetAt;

  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String email;

  @Builder.Default
  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private List<Assignment> assignments = new ArrayList<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private List<LastPassword> lastPasswords = new ArrayList<>();
}
