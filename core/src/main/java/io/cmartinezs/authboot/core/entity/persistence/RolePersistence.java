package io.cmartinezs.authboot.core.entity.persistence;

import io.cmartinezs.authboot.core.entity.domain.user.Role;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a role persistence object.
 */
@Getter
@Setter
@AllArgsConstructor
public class RolePersistence extends PersistenceBase {
    private String code;
    private String name;
    private String description;
    private Set<FunctionPersistence> functions;

    public Set<String> getFunctionsCodes() {
        return getFunctions()
                .stream()
                .map(FunctionPersistence::getCode)
                .collect(Collectors.toSet());
    }

    public Set<String> getFunctionTypeCodes() {
        return getFunctions()
                .stream()
                .map(FunctionPersistence::getType)
                .collect(Collectors.toSet());
    }

    public RolePersistence merge(RolePersistence newRole) {
        return new RolePersistence(
                newRole.getCode() != null ? newRole.getCode() : getCode(),
                newRole.getName() != null ? newRole.getName() : getName(),
                newRole.getDescription() != null ? newRole.getDescription() : getDescription(),
                newRole.getFunctions() != null ? newRole.getFunctions() : getFunctions()
        );
    }

    public Role toDomain() {
        Role role = new Role(
                getCode()
                , getName()
                , getDescription()
                , getFunctions().stream().map(FunctionPersistence::toDomain).collect(Collectors.toSet())
        );
        role.setCreatedAt(this.getCreatedAt());
        role.setUpdatedAt(this.getUpdatedAt());
        role.setEnabledAt(this.getEnabledAt());
        role.setDisabledAt(this.getDisabledAt());
        role.setExpiredAt(this.getExpiredAt());
        role.setLockedAt(this.getLockedAt());
        return role;
    }
}
