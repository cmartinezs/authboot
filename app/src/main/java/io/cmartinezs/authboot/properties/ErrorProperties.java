package io.cmartinezs.authboot.properties;

import io.cmartinezs.authboot.api.ControllerProperties;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "authboot.error")
@Getter
@Setter
public class ErrorProperties implements ControllerProperties {
  @Valid @NotBlank private String notificationEmail;
}
