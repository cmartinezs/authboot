package io.cmartinezs.authboot.infra.persistence.jpa.repository.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.FunctionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Carlos
 * @version 1.0
 */
@Repository
public interface FunctionTypeRepository extends JpaRepository<FunctionTypeEntity, Integer> {
    Set<FunctionTypeEntity> findByCodeIn(Set<String> functionTypeCodes);
}
