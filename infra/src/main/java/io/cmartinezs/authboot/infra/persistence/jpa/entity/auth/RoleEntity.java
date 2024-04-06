package io.cmartinezs.authboot.infra.persistence.jpa.entity.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.JpaEntity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "roles")
public class RoleEntity extends JpaEntity {

  @Column(name = "code", length = 10, nullable = false, unique = true)
  private String code;

  @Column(name = "name", length = 50, nullable = false, unique = true)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Builder.Default
  @OneToMany(mappedBy = "role")
  private Set<PermissionEntity> permissions = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoleEntity that)) return false;
    return getCode().equals(that.getCode());
  }

  @Override
  public int hashCode() {
    return getCode().hashCode();
  }
}
