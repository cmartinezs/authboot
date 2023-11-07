package io.cmartinezs.authboot.core.port.persistence;

import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import java.util.Optional;

/**
 * This interface is used to define the user persistence port.
 */
public interface UserPersistencePort {
    /**
     * Find a user by username
     *
     * @param username Username
     * @return Optional<UserPersistence>
     */
    Optional<UserPersistence> findByUsername(String username);

    /**
     * Save a user
     *
     * @param userPersistence UserPersistence
     * @return Integer
     */
    Integer save(UserPersistence userPersistence);
    UserPersistence edit(UserPersistence newUser, UserPersistence foundUser);
    void delete(UserPersistence foundUser);
    Optional<UserPersistence> findByEmail(String email);
    void updatePasswordRecoveryToken(String username, String token);
}
