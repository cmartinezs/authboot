package io.cmartinezs.authboot.core.entity.domain;

/**
 * @author Carlos
 * @version 1.0
 */
public interface CommonValidations {
  boolean isExpired();

  boolean isLocked();

  boolean isEnabled();
}
