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
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserJpaAdapter implements UserPersistencePort {

  private final AssigmentRepository assigmentRepository;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public static final String USER_ENTITY_NAME = "user";
  public static final String USERNAME_FIELD_NAME = "username";
  public static final String EMAIL_FIELD_NAME = "email";

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
  public UserPersistence findByUsername(String username) {
    return userRepository.findByUsername(username).map(PersistenceMapper::entityToPersistence)
            .orElseThrow(() -> new NotFoundEntityException(USER_ENTITY_NAME, USERNAME_FIELD_NAME, username));
  }

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
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
    var savedUserEntity =
        userRepository.save(PersistenceMapper.persistenceToEntity(userPersistence));
    var rolesByCode = mapRolesByCode(userPersistence);
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
                () -> new NotFoundEntityException(USER_ENTITY_NAME, USERNAME_FIELD_NAME, editedUser.getUsername()));
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
  public UserPersistence findByEmail(String email) {
    return userRepository.findByEmail(email).map(PersistenceMapper::entityToPersistence)
            .orElseThrow(() -> new NotFoundEntityException(USER_ENTITY_NAME, EMAIL_FIELD_NAME, email));
  }

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public void updatePasswordRecoveryToken(String username, String token, LocalDateTime expiredAt) {
    userRepository
        .findByUsername(username)
        .ifPresent(
            userEntity -> {
              userEntity.setPasswordRecoveryToken(token);
              userEntity.setPasswordRecoveryTokenExpiredAt(expiredAt);
              userRepository.save(userEntity);
            });
  }

  @Override
  public void updatePassword(String username, String cryptPassword) {
    userRepository
        .findByUsername(username)
        .ifPresent(
            userEntity -> {
              userEntity.setPassword(cryptPassword);
              userRepository.save(userEntity);
            });
  }
}
