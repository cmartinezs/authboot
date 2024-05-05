package io.cmartinezs.authboot.core.command.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailValidationCmd {
  private String username;
  private String email;
  private String validationCode;
}
