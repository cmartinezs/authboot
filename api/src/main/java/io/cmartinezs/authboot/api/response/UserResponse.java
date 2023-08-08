package io.cmartinezs.authboot.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserResponse {
    private final String username;
    private final String email;
    private final Set<String> authorities;
}
