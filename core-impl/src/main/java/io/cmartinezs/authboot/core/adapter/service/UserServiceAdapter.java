package io.cmartinezs.authboot.core.adapter.service;

import io.cmartinezs.authboot.core.command.CreateUserCmd;
import io.cmartinezs.authboot.core.command.DeleteUserCmd;
import io.cmartinezs.authboot.core.command.UpdateUserCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.core.exception.ExistsEntityException;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is an adapter for the UserServicePort interface.
 */
@RequiredArgsConstructor
public class UserServiceAdapter implements UserServicePort {
    private final UserPersistencePort userPersistencePort;

    @Override
    public Integer createUser(CreateUserCmd cmd) {
        if (userPersistencePort.findByUsername(cmd.getUsername()).isPresent()) {
            throw new ExistsEntityException(UserPersistence.class, "username", cmd.getUsername());
        }
        return userPersistencePort.save(toPersistence(cmd));
    }

    private static UserPersistence toPersistence(CreateUserCmd cmd) {
        return new UserPersistence(cmd.getUsername(), cmd.getPassword(), cmd.getEmail(), toPersistence(cmd.getRoles()));
    }

    private static Set<RolePersistence> toPersistence(Set<String> roles) {
        return Optional.ofNullable(roles)
                .stream()
                .flatMap(Set::stream)
                .map(role -> new RolePersistence(role, null, null))
                .collect(Collectors.toSet());
    }

    @Override
    public User deleteUser(DeleteUserCmd cmd) {

        return null;
    }

    @Override
    public User updateUser(UpdateUserCmd cmd) {

        return null;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userPersistencePort.findByUsername(username).map(User::new);
    }
}
