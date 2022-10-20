package io.cmartinezs.authboot.infra.persistence.repository.jpa.auth;

import io.cmartinezs.authboot.infra.persistence.entity.jpa.auth.Assignment;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Carlos
 * @version 1.0
 */
@Repository
public interface AssigmentRepository extends CrudRepository<Assignment, Integer> {
  List<Assignment> findAssignmentsByUserUsername(String username);
}
