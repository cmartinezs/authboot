package io.cmartinezs.authboot.infra.security.service;

import io.cmartinezs.authboot.core.command.LoginCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.infra.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Carlos
 * @version 1.0
 */
@RequiredArgsConstructor
public class AuthServiceAdapter implements AuthServicePort {

  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User authenticate(LoginCmd loginCmd) {
    var authentication = new UsernamePasswordAuthenticationToken(loginCmd.username(), loginCmd.password());
    var authenticate = authenticationManager.authenticate(authentication);
    return ((AppUserDetails) authenticate.getPrincipal()).user();
  }

  @Override
  public String encrypt(String password) {
    return passwordEncoder.encode(password);
  }
}
