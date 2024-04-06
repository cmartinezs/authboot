package io.cmartinezs.authboot.api.request.user;

import jakarta.validation.constraints.Email;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPostPasswordRecoveryRequest {
  @Email private String email;
}
