package io.cmartinezs.authboot.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPostCreateSuccess {
    private final Integer userId;
}
