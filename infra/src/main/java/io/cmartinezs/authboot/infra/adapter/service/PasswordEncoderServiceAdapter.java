package io.cmartinezs.authboot.infra.adapter.service;

import io.cmartinezs.authboot.core.port.service.PasswordEncoderServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Adapter for the password encoder service.
 *
 * <p>This adapter implements the password encoder port for the entity {@link
 * io.cmartinezs.authboot.core.entity.domain.user.User}. It uses the {@link
 * org.springframework.security.crypto.password.PasswordEncoder} to encrypt the password.
 *
 * @see PasswordEncoderServicePort
 * @see io.cmartinezs.authboot.core.entity.domain.user.User
 * @see org.springframework.security.crypto.password.PasswordEncoder
 */
@RequiredArgsConstructor
public class PasswordEncoderServiceAdapter implements PasswordEncoderServicePort {
  private final PasswordEncoder passwordEncoder;

  @Override
  public boolean matches(String oldPassword, String password) {
    return passwordEncoder.matches(oldPassword, password);
  }

  @Override
  public String encrypt(String password) {
    return passwordEncoder.encode(password);
  }
}
