package io.cmartinezs.authboot.api.request.user;

import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateUserRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    private String email;
    @Valid
    private Set<@NotBlank String> roles;
}
