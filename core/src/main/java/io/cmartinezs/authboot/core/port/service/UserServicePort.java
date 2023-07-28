package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.CreateUserCmd;
import io.cmartinezs.authboot.core.command.DeleteUserCmd;
import io.cmartinezs.authboot.core.command.UpdateUserCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;

import java.util.Optional;

/**
 * @author Carlos
 * @version 1.0
 */
public interface UserServicePort {
    Integer createUser(CreateUserCmd cmd);
    User deleteUser(DeleteUserCmd cmd);
    User updateUser(UpdateUserCmd cmd);
    Optional<User> findByUsername(String username);
}
