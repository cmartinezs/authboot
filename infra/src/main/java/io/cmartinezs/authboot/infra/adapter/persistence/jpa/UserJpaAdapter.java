package io.cmartinezs.authboot.infra.adapter.persistence.jpa;

import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.AssignmentEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.RoleEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.UserEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.AssigmentRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.RoleRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.UserRepository;
import io.cmartinezs.authboot.infra.utils.InfraCollections;
import io.cmartinezs.authboot.infra.utils.mapper.PersistenceMapper;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserJpaAdapter implements UserPersistencePort {

  private final AssigmentRepository assigmentRepository;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  /**
   * Creates a new {@link AssignmentEntity} with the given {@link UserEntity} and {@link
   * RoleEntity}.
   *
   * @param user The user to assign the role.
   * @param role The role to assign to the user.
   * @return The new {@link AssignmentEntity}.
   */
  private static AssignmentEntity newAssignment(UserEntity user, RoleEntity role) {
    var assignmentEntity = new AssignmentEntity();
    assignmentEntity.setUser(user);
    assignmentEntity.setRole(role);
    assignmentEntity.setEnabledAt(LocalDateTime.now());
    return assignmentEntity;
  }

  /**
   * Finds the {@link UserPersistence} with the given username.
   *
   * @param username The username of the user to find.
   * @return The {@link UserPersistence} with the given username.
   */
  @Override
  public Optional<UserPersistence> findByUsername(String username) {
    return userRepository.findByUsername(username).map(PersistenceMapper::entityToPersistence);
  }

  /**
   * Saves the given {@link UserPersistence}.
   *
   * @param userPersistence The user to save.
   * @return The id of the saved user.
   */
  @Override
  @Transactional
  public Integer save(UserPersistence userPersistence) {
    var rolesByCode = mapRolesByCode(userPersistence);
    var savedUserEntity =
        userRepository.save(PersistenceMapper.persistenceToEntity(userPersistence));
    createAssignments(savedUserEntity, userPersistence.getRoles(), rolesByCode);
    return savedUserEntity.getId();
  }

  /**
   * Edits the given {@link UserPersistence}.
   *
   * @param newUser The new user data.
   * @param foundUser The found user data.
   * @return The edited user.
   */
  @Override
  @Transactional
  public UserPersistence edit(UserPersistence newUser, UserPersistence foundUser) {
    var editedUser = foundUser.merge(newUser);
    var rolesByCode = mapRolesByCode(editedUser);
    var foundUserEntity = edit(editedUser);
    var assignments = createAssignments(foundUserEntity, editedUser.getRoles(), rolesByCode);
    editedUser.setRoles(PersistenceMapper.entityToAssigmentPersistence(assignments));
    return editedUser;
  }

  private Set<AssignmentEntity> createAssignments(
      UserEntity foundUserEntity, Set<RolePersistence> roles, Map<String, RoleEntity> rolesByCode) {
    // 1. Obtener las asignaciones actuales de foundUserEntity
    var currentAssignments = foundUserEntity.getAssignments();

    // 2. Identificar las asignaciones que se van a crear basados en roles
    var newAssignments =
        roles.stream()
            .map(role -> newAssignment(foundUserEntity, rolesByCode.get(role.getCode())))
            .collect(Collectors.toSet());

    // 3. Determinar qué asignaciones se deben eliminar y cuáles se deben agregar
    var assignmentsToDelete = new HashSet<>(currentAssignments);
    assignmentsToDelete.removeAll(newAssignments);

    var assignmentsToAdd = new HashSet<>(newAssignments);
    assignmentsToAdd.removeAll(currentAssignments);

    // 4. Eliminar y agregar las asignaciones según sea necesario
    if (!assignmentsToDelete.isEmpty()) {
      assigmentRepository.deleteAll(assignmentsToDelete);
      foundUserEntity.getAssignments().removeAll(assignmentsToDelete);
    }

    if (!assignmentsToAdd.isEmpty()) {
      assigmentRepository.saveAll(assignmentsToAdd);
      foundUserEntity.getAssignments().addAll(assignmentsToAdd);
    }

    userRepository.save(foundUserEntity);
    return newAssignments;
  }

  private UserEntity edit(UserPersistence editedUser) {
    var foundUserEntity =
        userRepository
            .findByUsername(editedUser.getUsername())
            .orElseThrow(
                () -> new NotFoundEntityException("user", "username", editedUser.getUsername()));
    foundUserEntity.setEmail(editedUser.getEmail());
    foundUserEntity.setPassword(editedUser.getPassword());
    userRepository.save(foundUserEntity);
    return foundUserEntity;
  }

  private Map<String, RoleEntity> mapRolesByCode(UserPersistence editedUser) {
    Set<NotFoundEntityException> errors = new HashSet<>();

    Map<String, RoleEntity> rolesByCode =
        InfraCollections.createMapFrom(
            editedUser.getRoleCodes(), roleRepository::findByCodeIn, RoleEntity::getCode);

    InfraCollections.validateMissingCodes(rolesByCode, editedUser.getRoleCodes(), "role")
        .ifPresent(errors::add);

    if (!errors.isEmpty()) {
      throw new NotFoundEntityException(errors);
    }

    return rolesByCode;
  }

  /**
   * Deletes the given {@link UserPersistence}.
   *
   * @param foundUser The user to delete.
   */
  @Override
  @Transactional
  public void delete(UserPersistence foundUser) {
    userRepository
        .findByUsername(foundUser.getUsername())
        .ifPresent(
            userEntity -> {
              Set<AssignmentEntity> byUserUsername =
                  assigmentRepository.findByUserUsername(userEntity.getUsername());
              assigmentRepository.deleteAll(byUserUsername);
              userRepository.delete(userEntity);
            });
  }

  @Override
  public Optional<UserPersistence> findByEmail(String email) {
    return userRepository.findByEmail(email).map(PersistenceMapper::entityToPersistence);
  }

  @Override
  public void updatePasswordRecoveryToken(String username, String token) {
    userRepository
        .findByUsername(username)
        .ifPresent(
            userEntity -> {
              userEntity.setPasswordRecoveryToken(token);
              userRepository.save(userEntity);
            });
  }
}
