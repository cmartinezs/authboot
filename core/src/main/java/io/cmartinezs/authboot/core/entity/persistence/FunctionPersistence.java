package io.cmartinezs.authboot.core.entity.persistence;

/**
 * This class represents a function persistence object.
 */
public record FunctionPersistence(String code, String name, String type, String typeName) implements PersistenceBase {}
