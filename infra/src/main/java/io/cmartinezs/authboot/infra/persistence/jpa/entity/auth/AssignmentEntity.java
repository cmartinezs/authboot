package io.cmartinezs.authboot.infra.persistence.jpa.entity.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.JpaEntity;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

/**
 * This class is the JPA entity for the assignments table.
 *
 * <p>This class is responsible for defining the assignments table.
 *
 * <p>The assignments table is defined with the following columns:
 *
 * <p>- user_id: The id of the user. - role_id: The id of the role.
 */
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
public class AssignmentEntity extends JpaEntity {
  @ManyToOne
  @JoinColumn(
      name = "user_id",
      updatable = false,
      nullable = false,
      foreignKey =
          @ForeignKey(name = "fk_assignment_user", value = ConstraintMode.PROVIDER_DEFAULT))
  private UserEntity user;

  @ManyToOne
  @JoinColumn(
      name = "role_id",
      updatable = false,
      nullable = false,
      foreignKey =
          @ForeignKey(name = "fk_assignment_role", value = ConstraintMode.PROVIDER_DEFAULT))
  private RoleEntity role;

  @Override
  public String toString() {
    return getUser().getUsername().concat("-").concat(getRole().getName());
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    AssignmentEntity that = (AssignmentEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
