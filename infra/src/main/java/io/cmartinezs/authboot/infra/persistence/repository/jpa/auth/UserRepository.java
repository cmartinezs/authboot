package io.cmartinezs.authboot.infra.persistence.repository.jpa.auth;

import io.cmartinezs.authboot.infra.persistence.entity.jpa.auth.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Carlos
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUsername(String username);
}
