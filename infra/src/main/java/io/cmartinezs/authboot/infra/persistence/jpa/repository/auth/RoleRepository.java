package io.cmartinezs.authboot.infra.persistence.jpa.repository.auth;

import io.cmartinezs.authboot.infra.persistence.jpa.entity.auth.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Set<RoleEntity> findByCodeIn(Set<String> codes);
}
