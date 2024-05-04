package io.cmartinezs.authboot.core.utils.service;

import io.cmartinezs.authboot.core.command.user.CreateUserCmd;
import io.cmartinezs.authboot.core.command.user.UpdateUserCmd;
import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class UserServiceUtils {
    public static Set<RolePersistence> toPersistence(Set<String> roles) {
        return Optional.ofNullable(roles).stream()
                .flatMap(Set::stream)
                .map(role -> new RolePersistence(role, null, null, null))
                .collect(Collectors.toSet());
    }

    public static UserPersistence newPersistence(CreateUserCmd cmd, String cryptPassword, int daysPwdExpiration, boolean enabledByDefault) {

        var user =
                new UserPersistence(
                        cmd.getUsername(), cmd.getEmail(), cryptPassword, toPersistence(cmd.getRoles()), null);
        if (enabledByDefault) {
            user.setEnabledAt(LocalDateTime.now());
            user.setPasswordResetAt(LocalDateTime.now().plusDays(daysPwdExpiration));
        }
        return user;
    }

    public static UserPersistence toPersistence(UpdateUserCmd cmd, String cryptPassword) {
        return new UserPersistence(
                cmd.getUsername(), cmd.getEmail(), cryptPassword, toPersistence(cmd.getRoles()), null);
    }
}
