package io.cmartinezs.authboot.api.response.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserGetByUsernameResponse {
  private final UserResponse user;
}
