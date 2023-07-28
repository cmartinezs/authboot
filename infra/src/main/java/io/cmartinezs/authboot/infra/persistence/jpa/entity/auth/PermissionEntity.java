package io.cmartinezs.authboot.infra.persistence.jpa.entity.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.JpaEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * This class is the JPA entity for the permissions table.
 * <p>
 * This class is responsible for defining the permissions table.
 * <p>
 * The permissions table is defined with the following columns:
 * <p>
 * - role_id: The id of the role.
 * - function_id: The id of the function.
 * - type_id: The id of the function type.
 */
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
public class PermissionEntity extends JpaEntity {

  @ManyToOne
  @JoinColumn(
      name = "role_id",
      nullable = false,
      foreignKey =
          @ForeignKey(name = "fk_permission_role", value = ConstraintMode.PROVIDER_DEFAULT))
  private RoleEntity role;

  @ManyToOne
  @JoinColumn(
      name = "function_id",
      nullable = false,
      foreignKey =
          @ForeignKey(name = "fk_permission_function", value = ConstraintMode.PROVIDER_DEFAULT))
  private FunctionEntity function;

  @ManyToOne
  @JoinColumn(
      name = "type_id",
      nullable = false,
      foreignKey =
          @ForeignKey(
              name = "fk_permission_function_type",
              value = ConstraintMode.PROVIDER_DEFAULT))
  private FunctionTypeEntity type;
}
