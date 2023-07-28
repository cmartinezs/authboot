package io.cmartinezs.authboot.core.exception;

import io.cmartinezs.authboot.core.entity.persistence.PersistenceBase;

public class ExistsEntityException extends PersistenceException {

    public static final String MSG_EXISTS_ENTITY = "The %s entity with %s=%s already exists";

    public ExistsEntityException(Class<? extends PersistenceBase> persistenceClass, String fieldName, String fieldValue) {
        super(persistenceClass.getSimpleName(), String.format(MSG_EXISTS_ENTITY, persistenceClass.getSimpleName(), fieldName, fieldValue));
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
