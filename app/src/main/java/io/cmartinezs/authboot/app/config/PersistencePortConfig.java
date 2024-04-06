package io.cmartinezs.authboot.app.config;

import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.infra.adapter.persistence.jpa.RoleJpaAdapter;
import io.cmartinezs.authboot.infra.adapter.persistence.jpa.UserJpaAdapter;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** This class is a configuration class for the persistence ports. */
@Configuration
@RequiredArgsConstructor
public class PersistencePortConfig {

  /**
   * This method creates a bean of type RolePersistencePort.
   *
   * @param roleRepository the RoleRepository.
   * @param permissionRepository the PermissionRepository.
   * @param functionRepository the FunctionRepository.
   * @param functionTypeRepository the FunctionTypeRepository.
   * @return a bean of type RolePersistencePort.
   */
  @Bean
  public RolePersistencePort rolePersistence(
      RoleRepository roleRepository,
      PermissionRepository permissionRepository,
      FunctionRepository functionRepository,
      FunctionTypeRepository functionTypeRepository) {
    return new RoleJpaAdapter(
        roleRepository, permissionRepository, functionRepository, functionTypeRepository);
  }

  /**
   * This method creates a bean of type UserPersistencePort.
   *
   * @param assigmentRepository the AssigmentRepository.
   * @param userRepository the UserRepository.
   * @param roleRepository the RoleRepository.
   * @return a bean of type UserPersistencePort.
   */
  @Bean
  public UserPersistencePort userPersistence(
      AssigmentRepository assigmentRepository,
      UserRepository userRepository,
      RoleRepository roleRepository) {
    return new UserJpaAdapter(assigmentRepository, userRepository, roleRepository);
  }
}
