package io.cmartinezs.authboot.core.entity.persistence;

import java.util.Set;

/**
 * This class represents a role persistence object.
 */
public record RolePersistence(String code, String name, Set<FunctionPersistence> functions) implements PersistenceBase  {}
