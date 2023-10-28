package io.cmartinezs.authboot.core.adapter.service;

import io.cmartinezs.authboot.core.command.user.CreateUserCmd;
import io.cmartinezs.authboot.core.command.user.DeleteUserCmd;
import io.cmartinezs.authboot.core.command.user.GetUserCmd;
import io.cmartinezs.authboot.core.command.user.UpdateUserCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import io.cmartinezs.authboot.core.exception.persistence.ExistsEntityException;
import io.cmartinezs.authboot.core.exception.persistence.NotFoundEntityException;
import io.cmartinezs.authboot.core.exception.service.MismatchedPassword;
import io.cmartinezs.authboot.core.port.persistence.UserPersistencePort;
import io.cmartinezs.authboot.core.port.service.PasswordEncoderServicePort;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import io.cmartinezs.authboot.core.utils.property.UserServiceProperties;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * This class is an adapter for the UserServicePort interface.
 */
@RequiredArgsConstructor
public class UserServiceAdapter implements UserServicePort {
    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderServicePort passwordEncoderService;
    private final UserServiceProperties properties;

    private static Set<RolePersistence> toPersistence(Set<String> roles) {
        return Optional.ofNullable(roles)
                .stream()
                .flatMap(Set::stream)
                .map(role -> new RolePersistence(role, null, null, null))
                .collect(Collectors.toSet());
    }

    private UserPersistence toPersistence(CreateUserCmd cmd) {
        var cryptPassword = passwordEncoderService.encrypt(cmd.getPassword());
        var userPersistence = new UserPersistence(cmd.getUsername(), cmd.getEmail(), cryptPassword, toPersistence(cmd.getRoles()));
        if (properties.isEnabledByDefault()) {
            userPersistence.setEnabledAt(LocalDateTime.now());
        }
        return userPersistence;
    }

    private UserPersistence toPersistence(UpdateUserCmd cmd) {
        String cryptPassword = null;
        if (cmd.getNewPassword() != null) {
            cryptPassword = passwordEncoderService.encrypt(cmd.getNewPassword());
        }
        return new UserPersistence(cmd.getUsername(), cmd.getEmail(), cryptPassword, toPersistence(cmd.getRoles()));
    }

    @Override
    public Integer createUser(CreateUserCmd cmd) {
        if (userPersistencePort.findByUsername(cmd.getUsername()).isPresent()) {
            throw new ExistsEntityException("user", "username", cmd.getUsername());
        }
        return userPersistencePort.save(toPersistence(cmd));
    }

    @Override
    public User updateUser(UpdateUserCmd cmd) {
        var foundUser = userPersistencePort.findByUsername(cmd.getUsername())
                .orElseThrow(() -> new NotFoundEntityException("user", "username", cmd.getUsername()));

        if (cmd.getOldPassword() != null
                && !passwordEncoderService.matches(cmd.getOldPassword(), foundUser.getPassword())) {
            throw new MismatchedPassword();
        }
        var editedUser = userPersistencePort.edit(toPersistence(cmd), foundUser);
        return new User(editedUser);
    }

    @Override
    public User deleteUser(DeleteUserCmd cmd) {
        var foundUser = userPersistencePort.findByUsername(cmd.getUsername())
                .orElseThrow(() -> new NotFoundEntityException("user", "username", cmd.getUsername()));
        userPersistencePort.delete(foundUser);
        return new User(foundUser);
    }

    @Override
    public User getUser(GetUserCmd cmd) {
        var username = cmd.getUsername();
        return userPersistencePort.findByUsername(username)
                .map(User::new)
                .orElseThrow(() -> new NotFoundEntityException("user", "username", username));
    }
}
