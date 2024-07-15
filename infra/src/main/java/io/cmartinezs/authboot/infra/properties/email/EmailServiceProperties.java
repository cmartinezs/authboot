package io.cmartinezs.authboot.infra.properties.email;

import io.cmartinezs.authboot.infra.properties.YamlPropertySourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties(prefix = "email")
@PropertySource(value = "configs/email.yml", factory = YamlPropertySourceFactory.class)
@Getter
@Setter
public class EmailServiceProperties {
  @NotEmpty
  Map<String, @Valid EmailDataData> userEmails;
}
