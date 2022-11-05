package io.cmartinezs.authboot.infra.security.service;

import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.infra.security.AppUserDetails;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Carlos
 * @version 1.0
 */
@RequiredArgsConstructor
public class JwtAuthUserDetailsService implements UserDetailsService {

  private final UserPersistencePort userPersistence;

  private static Supplier<UsernameNotFoundException> newUsernameNotFoundExceptionSupplier(
      String username) {
    return () -> new UsernameNotFoundException(String.format("Username '%s' not found", username));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userPersistence
        .findUserByUsername(username)
        .map(User::new)
        .map(AppUserDetails::new)
        .orElseThrow(newUsernameNotFoundExceptionSupplier(username));
  }
}
