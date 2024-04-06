package io.cmartinezs.authboot.api.request.user;

import java.util.Set;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPostRequest {
  @NotBlank private String username;
  @NotBlank private String password;
  @Email private String email;
  @Valid private Set<@NotBlank String> roles;
}
