package io.cmartinezs.authboot.api.request.user;

import javax.validation.constraints.Email;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPasswordRecoveryRequest {
    @Email
    private String email;
}
