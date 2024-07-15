package io.cmartinezs.authboot.api.request.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@Setter
public class LoginRequest {
  @NotBlank
  private String username;
  @NotBlank
  private String password;
}
