package io.cmartinezs.authboot.api.response.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPostSuccessResponse {
    private final Integer userId;
}
