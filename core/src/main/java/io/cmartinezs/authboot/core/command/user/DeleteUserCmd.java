package io.cmartinezs.authboot.core.command.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/** This class represents a command to delete a user. */
@Getter
@Setter
@RequiredArgsConstructor
public class DeleteUserCmd {
  private final String username;
}
