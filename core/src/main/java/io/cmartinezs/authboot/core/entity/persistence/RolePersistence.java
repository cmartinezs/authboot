package io.cmartinezs.authboot.core.entity.persistence;

import java.util.Set;

/**
 * @author Carlos
 * @version 1.0
 */
public record RolePersistence(String code, String name, Set<FunctionPersistence> functions) {}
