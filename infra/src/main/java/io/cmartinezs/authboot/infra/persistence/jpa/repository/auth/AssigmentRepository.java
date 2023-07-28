package io.cmartinezs.authboot.infra.persistence.jpa.repository.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.AssignmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Carlos
 * @version 1.0
 */
@Repository
public interface AssigmentRepository extends CrudRepository<AssignmentEntity, Integer> {
    Set<AssignmentEntity> findAssignmentsByUserUsername(String username);
}
