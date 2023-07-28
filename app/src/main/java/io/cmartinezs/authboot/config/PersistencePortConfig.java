package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.infra.adapter.persistence.jpa.AuthJpaAdapter;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.AssigmentRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.RoleRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is a configuration class for the persistence ports.
 */
@Configuration
@RequiredArgsConstructor
public class PersistencePortConfig {

  private final AssigmentRepository assigmentRepository;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

  /**
   * This method creates a bean of type RolePersistencePort.
   *
   * @return a bean of type RolePersistencePort.
   */
  @Bean
  public RolePersistencePort rolePersistence() {
    return getAuthJpaAdapter();
  }

  /**
   * This method creates a bean of type UserPersistencePort.
   *
   * @return a bean of type UserPersistencePort.
   */
  @Bean
  public UserPersistencePort userPersistence() {
    return getAuthJpaAdapter();
  }

  /**
   * This method creates a bean of type AuthJpaAdapter.
   *
   * @return a bean of type AuthJpaAdapter.
   */
  private AuthJpaAdapter getAuthJpaAdapter() {
    return new AuthJpaAdapter(assigmentRepository, userRepository, roleRepository);
  }
}
