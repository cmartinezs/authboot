package io.cmartinezs.authboot.core.entity.domain;

/** This interface is used to define the common validations for the domain entities. */
public interface CommonValidations {
  /** This method is used to validate the entity. */
  boolean isExpired();

  /** This method is used to validate the entity. */
  boolean isLocked();

  /** This method is used to validate the entity. */
  boolean isEnabled();
}
