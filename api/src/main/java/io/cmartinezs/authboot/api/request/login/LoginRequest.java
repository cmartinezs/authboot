package io.cmartinezs.authboot.api.request.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@Setter
public class LoginRequest {
  @NotEmpty private String username;
  @NotEmpty private String password;
}
