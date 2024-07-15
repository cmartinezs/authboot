package io.cmartinezs.authboot.app.properties;

import io.cmartinezs.authboot.infra.utils.properties.TokenProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
