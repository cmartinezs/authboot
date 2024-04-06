package io.cmartinezs.authboot.core.command.user;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/** This class represents a command to create a user. */
@Getter
@Setter
@RequiredArgsConstructor
public class CreateUserCmd {
  private final String username;
  private final String password;
  private final String email;
  private final Set<String> roles;
}
