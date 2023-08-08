package io.cmartinezs.authboot.infra.adapter.persistence.jpa;

import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.core.port.persistence.RolePersistencePort;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.AssignmentEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.RoleEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.UserEntity;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.AssigmentRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.RoleRepository;
import io.cmartinezs.authboot.infra.persistence.jpa.repository.auth.UserRepository;
import io.cmartinezs.authboot.infra.utils.mapper.PersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter for JPA persistence.
 * <p>
 * This adapter implements the persistence ports for the entities {@link RoleEntity} and {@link UserEntity}.
 * It uses the repositories {@link RoleRepository} and {@link UserRepository} to persist the entities.
 * </p>
 *
 * @see RolePersistencePort
 * @see UserPersistencePort
 * @see RoleEntity
 * @see UserEntity
 * @see AssignmentEntity
 * @see RoleRepository
 * @see UserRepository
 * @see AssigmentRepository
 * @see PersistenceMapper
 */
@RequiredArgsConstructor
public class AuthJpaAdapter implements RolePersistencePort, UserPersistencePort {

    private final AssigmentRepository assigmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Creates a new {@link AssignmentEntity} with the given {@link UserEntity} and {@link RoleEntity}.
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
     * Finds the set of {@link RolePersistence} with the given username.
     *
     * @param username The username of the roles to find.
     * @return The set {@link RolePersistence} with the given username.
     */
    @Override
    public Set<RolePersistence> findRolesByUsername(String username) {
        return PersistenceMapper.assignmentsToRoles(assigmentRepository.findByUserUsername(username));
    }

    /**
     * Finds the {@link UserPersistence} with the given username.
     *
     * @param username The username of the user to find.
     * @return The {@link UserPersistence} with the given username.
     */
    @Override
    public Optional<UserPersistence> findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(PersistenceMapper::persistenceToEntity);
    }

    /**
     * Saves the given {@link UserPersistence}.
     *
     * @param userPersistence The user to save.
     * @return The id of the saved user.
     */
    @Override
    public Integer save(UserPersistence userPersistence) {
        var savedUserEntity = userRepository.save(PersistenceMapper.persistenceToEntity(userPersistence));
        if (!userPersistence.getRoleCodes().isEmpty()) {
            saveNewAssignments(userPersistence, savedUserEntity);
        }
        return savedUserEntity.getId();
    }

    private Set<RolePersistence> saveNewAssignments(UserPersistence userPersistence, UserEntity savedUserEntity) {
        var newAssignmentEntities = roleRepository
                .findByCodeIn(userPersistence.getRoleCodes())
                .stream()
                .map(role -> newAssignment(savedUserEntity, role))
                .collect(Collectors.toSet());
        return PersistenceMapper.assignmentsToRoles(assigmentRepository.saveAll(newAssignmentEntities));
    }

    /**
     * Edits the given {@link UserPersistence}.
     *
     * @param newUser   The new user data.
     * @param foundUser The found user data.
     * @return The edited user.
     */
    @Override
    public UserPersistence edit(UserPersistence newUser, UserPersistence foundUser) {
        var editedUser = foundUser.merge(newUser);
        userRepository.findByUsername(editedUser.getUsername())
                .ifPresent(userEntity -> {
                    userEntity.setEmail(editedUser.getEmail());
                    userEntity.setPassword(editedUser.getPassword());
                    userRepository.save(userEntity);
                });
        return editedUser;
    }

    @Override
    @Transactional
    public void delete(UserPersistence foundUser) {
        userRepository.findByUsername(foundUser.getUsername())
                .ifPresent(userEntity -> {
                    Set<AssignmentEntity> byUserUsername = assigmentRepository.findByUserUsername(userEntity.getUsername());
                    assigmentRepository.deleteAll(byUserUsername);
                    userRepository.delete(userEntity);
                });
    }

    @Override
    @Transactional
    public Set<RolePersistence> assignNewRoles(UserPersistence user) {
        Set<RolePersistence> roles = new HashSet<>();
        userRepository.findByUsername(user.getUsername())
                .ifPresent(userEntity -> {
                    userEntity.getAssignments().clear();
                    userRepository.save(userEntity);
                    roles.addAll(saveNewAssignments(user, userEntity));
                });
        return roles;
    }
}
