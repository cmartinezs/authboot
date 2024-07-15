package io.cmartinezs.authboot.core.command.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/** This class represents a command to login. */
@Getter
@Setter
@RequiredArgsConstructor
public class LoginCmd {
  private final String username;
  private final String password;
}
