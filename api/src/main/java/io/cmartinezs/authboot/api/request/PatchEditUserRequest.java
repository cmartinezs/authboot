package io.cmartinezs.authboot.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

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
