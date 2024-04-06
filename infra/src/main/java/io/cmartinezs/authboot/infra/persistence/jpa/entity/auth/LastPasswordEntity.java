package io.cmartinezs.authboot.infra.persistence.jpa.entity.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class is the JPA entity for the last_passwords table.
 *
 * <p>This class is responsible for defining the last_passwords table.
 *
 * <p>The last_passwords table is defined with the following columns:
 *
 * <p>- user_id: The id of the user. - password: The password of the user.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "last_passwords")
public class LastPasswordEntity extends JpaEntity {
  @ManyToOne
  @JoinColumn(
      name = "user_id",
      updatable = false,
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_last_pwds_user", value = ConstraintMode.PROVIDER_DEFAULT))
  private UserEntity user;

  @Column(name = "password", length = 100, nullable = false)
  private String password;
}
