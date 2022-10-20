package io.cmartinezs.authboot.properties;

import io.cmartinezs.authboot.core.properties.TokenProperties;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Carlos
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JsonWebTokenProperties implements TokenProperties {
  @Valid @NotBlank private String secret;
  @Valid @NotBlank private String secretEncodingType;
  @Valid @NotNull private long expiration;
  @Valid @NotBlank private String issuer;
}
