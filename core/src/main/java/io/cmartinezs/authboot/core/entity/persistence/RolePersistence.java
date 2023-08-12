package io.cmartinezs.authboot.core.entity.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a role persistence object.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class RolePersistence extends PersistenceBase {
    private final String code;
    private final String name;
    private final String description;
    private final Set<FunctionPersistence> functions;

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
}
