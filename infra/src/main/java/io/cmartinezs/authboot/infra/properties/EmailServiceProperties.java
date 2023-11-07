package io.cmartinezs.authboot.infra.properties;

import io.cmartinezs.authboot.infra.utils.properties.EmailProperties;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "email")
@PropertySource(value = "classpath:email.yml", factory = YamlPropertySourceFactory.class)
@Getter
@Setter
public class EmailServiceProperties implements EmailProperties {
  private Map<String, EmailTemplateProperties> templates;
  private Map<String, UriProperties> uris;
}
