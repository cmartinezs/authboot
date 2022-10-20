package io.cmartinezs.authboot.infra.persistence.entity.jpa.auth;

import io.cmartinezs.authboot.infra.persistence.entity.jpa.JpaEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "functions")
public class Function extends JpaEntity {

  @Column(name = "code", length = 50, nullable = false)
  private String code;

  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Override
  public boolean equals(Object obj) {
    if (Objects.isNull(obj)) return false;
    if (!(obj instanceof Function)) return false;
    return this.getId().equals(((Function) obj).getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, name);
  }
}
