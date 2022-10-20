package io.cmartinezs.authboot.infra.persistence.entity.jpa.auth;

import io.cmartinezs.authboot.infra.persistence.entity.jpa.JpaEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "roles")
public class Role extends JpaEntity {

  @Column(name = "code", length = 10, nullable = false, unique = true)
  private String code;

  @Column(name = "name", length = 50, nullable = false, unique = true)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Builder.Default
  @OneToMany(mappedBy = "role")
  private Set<Permission> permissions = Set.of();
}
