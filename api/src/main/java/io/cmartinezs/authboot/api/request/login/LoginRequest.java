package io.cmartinezs.authboot.api.request.login;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@Setter
public class LoginRequest {
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;
}