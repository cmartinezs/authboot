package io.cmartinezs.authboot.core.port.service;

import io.cmartinezs.authboot.core.command.user.CreateUserCmd;
import io.cmartinezs.authboot.core.command.user.DeleteUserCmd;
import io.cmartinezs.authboot.core.command.user.GetUserCmd;
import io.cmartinezs.authboot.core.command.user.UpdateUserCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;

/**
 * @author Carlos
 * @version 1.0
 */
public interface UserServicePort {
    Integer createUser(CreateUserCmd cmd);
    User deleteUser(DeleteUserCmd cmd);
    User updateUser(UpdateUserCmd cmd);
    User getUser(GetUserCmd cmd);
}
