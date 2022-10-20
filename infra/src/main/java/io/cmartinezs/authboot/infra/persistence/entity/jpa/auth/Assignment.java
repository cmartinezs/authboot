package io.cmartinezs.authboot.infra.persistence.entity.jpa.auth;

import io.cmartinezs.authboot.infra.persistence.entity.jpa.JpaEntity;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(
    name = "assignments",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_user_role",
          columnNames = {"user_id", "role_id"})
    })
public class Assignment extends JpaEntity {
  @ManyToOne
  @JoinColumn(
      name = "user_id",
      updatable = false,
      nullable = false,
      foreignKey =
          @ForeignKey(name = "fk_assignment_user", value = ConstraintMode.PROVIDER_DEFAULT))
  private User user;

  @ManyToOne
  @JoinColumn(
      name = "role_id",
      updatable = false,
      nullable = false,
      foreignKey =
          @ForeignKey(name = "fk_assignment_role", value = ConstraintMode.PROVIDER_DEFAULT))
  private Role role;

  @Override
  public String toString() {
    return getUser().getUsername().concat("-").concat(getRole().getName());
  }
}
