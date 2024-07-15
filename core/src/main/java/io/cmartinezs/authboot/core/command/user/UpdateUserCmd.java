package io.cmartinezs.authboot.core.command.user;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/** This class represents a command to create a user. */
@Getter
@Setter
@Builder
public class UpdateUserCmd {
  private String username;
  private String newPassword;
  private String oldPassword;
  private String email;
  private Set<String> roles;

  public boolean hasRoles() {
    return this.getRoles() != null && !this.getRoles().isEmpty();
  }
}
