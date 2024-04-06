package io.cmartinezs.authboot.api.response.user;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
  private final String username;
  private final String email;
  private final Set<String> authorities;
}
