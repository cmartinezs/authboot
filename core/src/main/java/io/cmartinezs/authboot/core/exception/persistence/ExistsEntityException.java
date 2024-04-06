package io.cmartinezs.authboot.core.exception.persistence;


public class ExistsEntityException extends PersistenceException {

    public static final String MSG_EXISTS_ENTITY = "The %s entity with %s=%s already exists";

    public ExistsEntityException(String entityName, String fieldName, String fieldValue) {
        super(entityName, String.format(MSG_EXISTS_ENTITY, entityName, fieldName, fieldValue));
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
