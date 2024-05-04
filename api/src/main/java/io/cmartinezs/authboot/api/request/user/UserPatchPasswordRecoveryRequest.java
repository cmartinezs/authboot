package io.cmartinezs.authboot.api.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPatchPasswordRecoveryRequest {
    private final String recoveryCode;
    private final String password;
    private final String confirmPassword;
}
