package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.request.JwtLoginRequest;
import io.cmartinezs.authboot.api.response.JwtLoginSuccess;
import io.cmartinezs.authboot.api.response.MessageResponse;
import io.cmartinezs.authboot.core.command.JwtGenerateCmd;
import io.cmartinezs.authboot.core.command.LoginCmd;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Carlos
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/jwt")
public class JsonWebTokenController {

  private final AuthServicePort authService;
  private final TokenServicePort tokenService;

  @PostMapping("/login")
  public ResponseEntity<JwtLoginSuccess> login(@RequestBody @Valid JwtLoginRequest loginRequest) {
    var loginCmd = new LoginCmd(loginRequest.username(), loginRequest.password());
    var loginUser = authService.authenticate(loginCmd);
    var jwtGenerateCmd = new JwtGenerateCmd(loginUser.getUsername(), loginUser.getAuthorities());
    var token = tokenService.generate(jwtGenerateCmd);
    var jwtLoginSuccess =
        JwtLoginSuccess.builder()
            .success(new MessageResponse("S00", "Success authentication"))
            .token(token)
            .build();
    return ResponseEntity.ok(jwtLoginSuccess);
  }
}
