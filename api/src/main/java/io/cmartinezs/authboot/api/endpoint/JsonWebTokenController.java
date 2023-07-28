package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.request.JwtLoginRequest;
import io.cmartinezs.authboot.api.response.JwtEncryptPasswordSuccess;
import io.cmartinezs.authboot.api.response.JwtLoginSuccess;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.core.command.JwtGenerateCmd;
import io.cmartinezs.authboot.core.command.LoginCmd;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This class represents a controller to handle JWT requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/jwt")
public class JsonWebTokenController {

    private final AuthServicePort authService;
    private final TokenServicePort tokenService;

    /**
     * This method handles a login request.
     *
     * @param loginRequest The login request.
     * @return A response entity with a JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody @Valid JwtLoginRequest loginRequest) {
        var loginCmd = new LoginCmd(loginRequest.getUsername(), loginRequest.getPassword());
        var loginUser = authService.authenticate(loginCmd);
        var jwtGenerateCmd = new JwtGenerateCmd(loginUser.getUsername(), loginUser.getAuthorities());
        var token = tokenService.generate(jwtGenerateCmd);
        var response =
                BaseResponse.builder()
                        .success(new MessageResponse("S00", "Success authentication"))
                        .data(new JwtLoginSuccess(loginUser.getUsername(), loginUser.getAuthorities(), token))
                        .build();
        return ResponseEntity.ok(response);
    }

    /**
     * This method handles a password encrypt request.
     *
     * @param password The password to encrypt.
     * @return A response entity with the encrypted password.
     */
    @GetMapping("/encrypt/{password}")
    public ResponseEntity<BaseResponse> encryptPassword(@PathVariable String password) {
        var encryptPassword = authService.encrypt(password);
        var response = BaseResponse.builder()
                .success(new MessageResponse("S00", "Success password encrypt"))
                .data(new JwtEncryptPasswordSuccess(password, encryptPassword))
                .build();
        return ResponseEntity.ok(response);
    }
}
