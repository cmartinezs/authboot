package io.cmartinezs.authboot.infra.persistence.jpa.entity.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.JpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * This class is the JPA entity for the roles table.
 * <p>
 * This class is responsible for defining the roles table.
 * <p>
 * The roles table is defined with the following columns:
 * <p>
 * - code: The code of the role.
 * - name: The name of the role.
 * - description: The description of the role.
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
  private Set<PermissionEntity> permissions = Set.of();
}
