package io.cmartinezs.authboot.infra.persistence.entity.jpa.auth;

import io.cmartinezs.authboot.infra.persistence.entity.jpa.JpaEntity;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "last_passwords")
public class LastPassword extends JpaEntity {
  @ManyToOne
  @JoinColumn(
      name = "user_id",
      updatable = false,
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_last_pwds_user", value = ConstraintMode.PROVIDER_DEFAULT))
  private User user;

  @Column(name = "password", length = 100, nullable = false)
  private String password;
}
