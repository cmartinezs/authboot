package io.cmartinezs.authboot.core.port.persistence;

import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import java.util.Set;

/**
 * @author Carlos
 * @version 1.0
 */
public interface RolePersistencePort {
  Set<RolePersistence> findRolesByUsername(String username);
}
