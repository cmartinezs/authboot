package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.request.login.LoginRequest;
import io.cmartinezs.authboot.api.response.auth.AuthEncryptPasswordSuccessResponse;
import io.cmartinezs.authboot.api.response.auth.AuthLoginSuccessResponse;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.core.command.auth.GenerateTokenCmd;
import io.cmartinezs.authboot.core.command.auth.LoginCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.core.port.service.PasswordEncoderServicePort;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class represents a controller to handle JWT requests. It contains a login endpoint and a
 * password encrypt endpoint.
 *
 * <p>The login endpoint receives a login request and returns a JWT. The password encrypt endpoint
 * receives a password and returns the encrypted password. Both endpoints return a response entity
 * with a base response. The base response contains a success message and a data object.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthServicePort authService;
  private final PasswordEncoderServicePort passwordEncoderService;
  private final TokenServicePort tokenService;

  private static AuthLoginSuccessResponse getLoginSuccess(User loginUser, String token) {
    return new AuthLoginSuccessResponse(
        loginUser.getUsername(),
        loginUser.getEmail(),
        loginUser.getRoles(),
        loginUser.getAuthorities(),
        token);
  }

  /**
   * This method handles a login request. It receives a login request and returns a JWT.
   *
   * <p>The login request contains a username and a password. The username and password are used to
   * authenticate the user. If the authentication is successful, a JWT is generated. The JWT
   * contains the username, email, roles and expiration date. The JWT is returned in the response.
   * The response contains a base response with a success message and a data object.
   *
   * @param loginRequest The login request.
   * @return A response entity with a base response.
   */
  @PostMapping
  public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
    var loginCmd = new LoginCmd(loginRequest.getUsername(), loginRequest.getPassword());
    var loginUser = authService.authenticate(loginCmd);
    var generateTokenCmd =
        new GenerateTokenCmd(loginUser.getUsername(), loginUser.getAuthorities());
    var token = tokenService.generate(generateTokenCmd);
    var response =
        BaseResponse.builder()
            .success(new MessageResponse("S00", "Success authentication"))
            .data(getLoginSuccess(loginUser, token))
            .build();
    return ResponseEntity.ok(response);
  }

  /**
   * This method handles a password encrypt request. It receives a password and returns the
   * encrypted password.
   *
   * <p>The password is encrypted using the password encoder service. The encrypted password is
   * returned in the response. The response contains a base response with a success message and a
   * data object.
   *
   * @param password The password.
   * @return A response entity with a base response.
   */
  @GetMapping("/encrypt/{password}")
  public ResponseEntity<BaseResponse> encryptPassword(@PathVariable String password) {
    var encryptPassword = passwordEncoderService.encrypt(password);
    var response =
        BaseResponse.builder()
            .success(new MessageResponse("S00", "Success password encrypt"))
            .data(new AuthEncryptPasswordSuccessResponse(password, encryptPassword))
            .build();
    return ResponseEntity.ok(response);
  }
}
