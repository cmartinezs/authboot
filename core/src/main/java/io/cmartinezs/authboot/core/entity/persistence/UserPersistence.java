package io.cmartinezs.authboot.core.entity.persistence;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a user persistence object.
 */
@Getter
@Setter
public class UserPersistence implements PersistenceBase {
    private String username;
    private String email;
    private String password;
    private Set<RolePersistence> roles;
    private LocalDateTime enabledAt;
    private LocalDateTime expiredAt;
    private LocalDateTime lockedAt;
    private LocalDateTime passwordResetAt;

    public UserPersistence(String username, String email, String password, Set<RolePersistence> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    /**
     * This method returns the role codes of the user.
     *
     * @return a set of role codes.
     */
    public Set<String> getRoleCodes() {
        return getRoles().stream().map(RolePersistence::code).collect(Collectors.toSet());
    }

    public UserPersistence merge(UserPersistence another) {
        return new UserPersistence(
                another.getUsername() != null ? another.getUsername() : getUsername(),
                another.getEmail() != null ? another.getEmail() : getEmail(),
                another.getPassword() != null ? another.getPassword() : getPassword(),
                another.getRoles() != null ? another.getRoles() : getRoles()
        );
    }
}
