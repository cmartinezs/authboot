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
    name = "permissions",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_role_function_type",
          columnNames = {"role_id", "function_id", "type_id"})
    })
public class Permission extends JpaEntity {

  @ManyToOne
  @JoinColumn(
      name = "role_id",
      nullable = false,
      foreignKey =
          @ForeignKey(name = "fk_permission_role", value = ConstraintMode.PROVIDER_DEFAULT))
  private Role role;

  @ManyToOne
  @JoinColumn(
      name = "function_id",
      nullable = false,
      foreignKey =
          @ForeignKey(name = "fk_permission_function", value = ConstraintMode.PROVIDER_DEFAULT))
  private Function function;

  @ManyToOne
  @JoinColumn(
      name = "type_id",
      nullable = false,
      foreignKey =
          @ForeignKey(
              name = "fk_permission_function_type",
              value = ConstraintMode.PROVIDER_DEFAULT))
  private FunctionType type;
}
