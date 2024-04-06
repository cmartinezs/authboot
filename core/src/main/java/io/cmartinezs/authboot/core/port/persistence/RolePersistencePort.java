package io.cmartinezs.authboot.core.port.persistence;

import io.cmartinezs.authboot.core.entity.persistence.RolePersistence;
import java.util.Optional;
import java.util.Set;

/**
 * This interface is used to define the role persistence port.
 */
public interface RolePersistencePort {
    Set<RolePersistence> findAll();

    Optional<RolePersistence> findByCode(String code);

    Integer save(RolePersistence rolePersistence);

    RolePersistence edit(RolePersistence rolePersistence, RolePersistence persistence);

    void delete(RolePersistence foundRole);
}
