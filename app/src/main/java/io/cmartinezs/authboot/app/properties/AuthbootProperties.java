package io.cmartinezs.authboot.app.properties;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "authboot")
@Getter
@Setter
public class AuthbootProperties {
  @Valid @NotBlank private String profile;
  @Valid @NotBlank private String version;
}
