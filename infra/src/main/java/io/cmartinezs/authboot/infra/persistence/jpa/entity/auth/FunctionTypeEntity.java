package io.cmartinezs.authboot.infra.persistence.jpa.entity.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.JpaEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class is the JPA entity for the function_types table.
 * <p>
 * This class is responsible for defining the function_types table.
 * <p>
 * The function_types table is defined with the following columns:
 * <p>
 * - code: The code of the function type.
 * - name: The name of the function type.
 * - description: The description of the function type.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "function_types")
public class FunctionTypeEntity extends JpaEntity {
  @Column(name = "code", length = 10, nullable = false, unique = true)
  private String code;

  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "description", length = 500)
  private String description;
}
