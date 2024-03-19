package io.cmartinezs.authboot.api.request.user;

import java.util.Set;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPatchRequest {
    private String oldPassword;
    private String newPassword;
    private String email;
    @Valid
    private Set<@NotBlank String> roles;
}
