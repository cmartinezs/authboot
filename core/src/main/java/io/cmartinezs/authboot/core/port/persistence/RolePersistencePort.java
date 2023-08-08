package io.cmartinezs.authboot.core.port.persistence;

import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;

import java.util.Set;

/**
 * This interface is used to define the role persistence port.
 */
public interface RolePersistencePort {
  Set<RolePersistence> findRolesByUsername(String username);
}
