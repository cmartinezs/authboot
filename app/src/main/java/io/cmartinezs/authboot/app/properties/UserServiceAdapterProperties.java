package io.cmartinezs.authboot.app.properties;

import io.cmartinezs.authboot.core.utils.property.UserServiceProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "authboot.services.user")
@Getter
@Setter
public class UserServiceAdapterProperties implements UserServiceProperties {
    private boolean enabledByDefault;
}
