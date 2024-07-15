package io.cmartinezs.authboot.app.properties;

import io.cmartinezs.authboot.infra.utils.properties.SecurityProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "authboot.security-adapter")
@Getter
@Setter
public class SecurityAdapterProperties implements SecurityProperties {
  private boolean enableHttpBasic;
}
