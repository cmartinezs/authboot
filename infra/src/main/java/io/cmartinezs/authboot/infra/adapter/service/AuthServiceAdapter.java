package io.cmartinezs.authboot.infra.adapter.service;

import io.cmartinezs.authboot.core.command.LoginCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import io.cmartinezs.authboot.infra.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * Adapter for the authentication service.
 * <p>
 *     This adapter implements the authentication port for the entity {@link User}.
 *     It uses the {@link AuthenticationManager} to authenticate the user.
 * </p>
 *
 * @see AuthServicePort
 * @see User
 * @see AuthenticationManager
 */
@RequiredArgsConstructor
public class AuthServiceAdapter implements AuthServicePort {

  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserServicePort userServicePort;

  /**
   * Authenticates the user with the given credentials.
   *
   * @param loginCmd The credentials of the user.
   * @return The authenticated user.
   */
  @Override
  public User authenticate(LoginCmd loginCmd) {
    var authentication = new UsernamePasswordAuthenticationToken(loginCmd.getUsername(), loginCmd.getPassword());
    var authenticate = authenticationManager.authenticate(authentication);
    return ((AppUserDetails) authenticate.getPrincipal()).user();
  }

  /**
   * Encrypts the given password.
   *
   * @param password The password to encrypt.
   * @return The encrypted password.
   */
  @Override
  public String encrypt(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  public Optional<User> getByUsername(String username) {
    return userServicePort.findByUsername(username);
  }
}
