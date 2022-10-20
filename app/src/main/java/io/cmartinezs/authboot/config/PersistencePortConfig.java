package io.cmartinezs.authboot.config;

import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.infra.persistence.adapter.jpa.AuthJpaAdapter;
import io.cmartinezs.authboot.infra.persistence.repository.jpa.auth.AssigmentRepository;
import io.cmartinezs.authboot.infra.persistence.repository.jpa.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Carlos
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class PersistencePortConfig {

  private final AssigmentRepository assigmentRepository;
  private final UserRepository userRepository;

  @Bean
  public RolePersistencePort rolePersistence() {
    return getAuthJpaAdapter();
  }

  @Bean
  public UserPersistencePort userPersistence() {
    return getAuthJpaAdapter();
  }

  private AuthJpaAdapter getAuthJpaAdapter() {
    return new AuthJpaAdapter(assigmentRepository, userRepository);
  }
}
