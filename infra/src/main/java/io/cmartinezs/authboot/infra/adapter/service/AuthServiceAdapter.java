package io.cmartinezs.authboot.infra.adapter.service;

import io.cmartinezs.authboot.core.command.auth.LoginCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.infra.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Adapter for the authentication service.
 *
 * <p>This adapter implements the authentication port for the entity {@link User}. It uses the
 * {@link AuthenticationManager} to authenticate the user.
 *
 * @see AuthServicePort
 * @see User
 * @see AuthenticationManager
 */
@RequiredArgsConstructor
public class AuthServiceAdapter implements AuthServicePort {

  private final AuthenticationManager authenticationManager;

  /**
   * Authenticates the user with the given credentials.
   *
   * @param loginCmd The credentials of the user.
   * @return The authenticated user.
   */
  @Override
  public User authenticate(LoginCmd loginCmd) {
    var authentication =
        new UsernamePasswordAuthenticationToken(loginCmd.getUsername(), loginCmd.getPassword());
    var authenticate = authenticationManager.authenticate(authentication);
    return ((AppUserDetails) authenticate.getPrincipal()).getUser();
  }
}
