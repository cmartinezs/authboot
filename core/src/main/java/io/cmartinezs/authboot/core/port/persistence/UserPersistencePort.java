package io.cmartinezs.authboot.core.port.persistence;

import io.cmartinezs.authboot.core.entity.persistence.UserPersistence;
import java.util.Optional;

/**
 * @author Carlos
 * @version 1.0
 */
public interface UserPersistencePort {
  Optional<UserPersistence> findUserByUsername(String username);
}
