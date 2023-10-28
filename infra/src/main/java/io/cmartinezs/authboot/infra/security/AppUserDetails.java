package io.cmartinezs.authboot.infra.security;

import io.cmartinezs.authboot.core.entity.domain.user.User;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {
  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return !user.isExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !user.isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !user.mustChangePassword();
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }
}
