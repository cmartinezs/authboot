package io.cmartinezs.authboot.infra.persistence.jpa.repository.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.FunctionTypeEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Carlos
 * @version 1.0
 */
@Repository
public interface FunctionTypeRepository extends JpaRepository<FunctionTypeEntity, Integer> {
  Set<FunctionTypeEntity> findByCodeIn(Set<String> functionTypeCodes);
}
