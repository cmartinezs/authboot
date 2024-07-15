package io.cmartinezs.authboot.infra.persistence.jpa.repository.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.FunctionEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Carlos
 * @version 1.0
 */
@Repository
public interface FunctionRepository extends JpaRepository<FunctionEntity, Integer> {
  Set<FunctionEntity> findByCodeIn(Set<String> functionsCodes);
}
