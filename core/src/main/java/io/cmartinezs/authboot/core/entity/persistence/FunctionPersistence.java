package io.cmartinezs.authboot.core.entity.persistence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class represents a function persistence object.
 */
@Getter
@RequiredArgsConstructor
public class FunctionPersistence extends PersistenceBase {
    private final String code;
    private final String name;
    private final String type;
    private final String typeName;
}