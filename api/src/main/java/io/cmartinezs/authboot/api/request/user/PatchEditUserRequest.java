package io.cmartinezs.authboot.api.request.user;

import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PatchEditUserRequest {
    @NotBlank
    private String username;
    private String oldPassword;
    private String newPassword;
    private String email;
    @Valid
    private Set<@NotBlank String> roles;
}
