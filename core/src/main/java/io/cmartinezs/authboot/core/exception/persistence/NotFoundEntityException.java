package io.cmartinezs.authboot.core.exception.persistence;

import io.cmartinezs.authboot.core.entity.persistence.PersistenceBase;

public class NotFoundEntityException extends PersistenceException {

    public static final String MSG_NOT_FOUND_ENTITY = "The %s entity with %s=%s does not exist";

    public NotFoundEntityException(Class<? extends PersistenceBase> persistenceClass, String fieldName, String fieldValue) {
        super(persistenceClass.getSimpleName(), String.format(MSG_NOT_FOUND_ENTITY, persistenceClass.getSimpleName(), fieldName, fieldValue));
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
