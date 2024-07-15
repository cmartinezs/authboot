package io.cmartinezs.authboot.core.entity.domain.user;

/** This interface is used to define the user validations. */
public interface UserValidations {
  /** This method is used to validate the user. */
  boolean mustChangePassword();
}
