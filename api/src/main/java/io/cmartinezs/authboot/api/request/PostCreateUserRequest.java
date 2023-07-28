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
public class PostCreateUserRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @Valid
    private Set<@NotBlank String> roles;
}
