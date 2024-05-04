package io.cmartinezs.authboot.core.utils.property;

public interface UserServiceProperties {
  boolean isEnabledByDefault();

  int getMinutesValidationCreateUser();

  int getMinutesPasswordRecovery();

  int getDaysPasswordExpiration();
}
