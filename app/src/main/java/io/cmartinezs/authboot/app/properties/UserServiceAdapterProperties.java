package io.cmartinezs.authboot.app.properties;

import io.cmartinezs.authboot.core.utils.property.UserServiceProperties;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties(prefix = "authboot.services.user")
@Getter
@Setter
public class UserServiceAdapterProperties implements UserServiceProperties {
  private boolean enabledByDefault;

  @Min(value = 1, message = "The validation minutes must be greater than 0")
  private int minutesValidationCreateUser;

  @Min(value = 1, message = "The minutes password recovery must be greater than 0")
  private int minutesPasswordRecovery;

  @Min(value = 1, message = "The days password expiration must be greater than 0")
  private int daysPasswordExpiration;
}
