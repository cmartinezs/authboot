package io.cmartinezs.authboot.infra.security;

import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.UserRepository;
import io.cmartinezs.authboot.infra.utils.mapper.PersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Carlos
 * @version 1.0
 */
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .map(PersistenceMapper::entityToPersistence)
        .map(User::new)
        .map(AppUserDetails::new)
        .orElseThrow(
            () ->
                new UsernameNotFoundException(String.format("Username '%s' not found", username)));
  }
}
