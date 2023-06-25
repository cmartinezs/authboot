package io.cmartinezs.authboot.api.request;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@Setter
public class JwtLoginRequest {
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;
}
