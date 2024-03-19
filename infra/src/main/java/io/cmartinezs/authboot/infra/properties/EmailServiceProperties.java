package io.cmartinezs.authboot.infra.properties;

import io.cmartinezs.authboot.infra.utils.properties.EmailProperties;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties(prefix = "email")
@PropertySource(value = "email.yml", factory = YamlPropertySourceFactory.class)
@Getter
@Setter
public class EmailServiceProperties implements EmailProperties {
  @NotEmpty
  private Map<String, @Valid BaseEmailSenderInfo> senderInfo;
  @NotEmpty
  private Map<String, @Valid UriProperties> uris;
}
