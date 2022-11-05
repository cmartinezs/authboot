package io.cmartinezs.authboot.infra.persistence.adapter.jpa;

import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.infra.persistence.repository.jpa.auth.AssigmentRepository;
import io.cmartinezs.authboot.infra.persistence.repository.jpa.auth.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * @author Carlos
 * @version 1.0
 */
@RequiredArgsConstructor
public class AuthJpaAdapter implements RolePersistencePort, UserPersistencePort {

  private final AssigmentRepository assigmentRepository;
  private final UserRepository userRepository;

  @Override
  public Set<RolePersistence> findRolesByUsername(String username) {
    return assigmentRepository.findAssignmentsByUserUsername(username).stream()
        .map(AuthJpaAdapterUtils.toRolePersistence())
        .collect(Collectors.toSet());
  }

  @Override
  public Optional<UserPersistence> findUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .map(AuthJpaAdapterUtils.toUserPersistence(findRolesByUsername(username)));
  }
}
