package io.cmartinezs.authboot.infra.persistence.jpa.entity.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.JpaEntity;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class is the JPA entity for the functions table.
 *
 * <p>This class is responsible for defining the functions table.
 *
 * <p>The functions table is defined with the following columns:
 *
 * <p>- code: The code of the function. - name: The name of the function. - description: The
 * description of the function.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "functions")
public class FunctionEntity extends JpaEntity {

  @Column(name = "code", length = 50, nullable = false)
  private String code;

  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Override
  public int hashCode() {
    return getCode().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FunctionEntity that)) return false;
    return getCode().equals(that.getCode());
  }
}
