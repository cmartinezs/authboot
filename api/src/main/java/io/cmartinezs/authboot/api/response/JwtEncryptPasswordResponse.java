package io.cmartinezs.authboot.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@SuperBuilder
public class JwtEncryptPasswordResponse extends BaseResponse {
  @NotEmpty
  private final String password;
  @NotEmpty
  @JsonProperty("encrypt_password")
  private final String encryptPassword;
}
