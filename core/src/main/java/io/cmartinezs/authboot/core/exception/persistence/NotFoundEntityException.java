package io.cmartinezs.authboot.core.exception.persistence;

import java.util.Set;
import java.util.stream.Collectors;

public class NotFoundEntityException extends PersistenceException {

  public static final String MSG_NOT_FOUND_ENTITY = "The %s entity with %s=%s does not exist";

  public NotFoundEntityException(String entityName, String fieldName, String fieldValue) {
    super(entityName, String.format(MSG_NOT_FOUND_ENTITY, entityName, fieldName, fieldValue));
  }

  public NotFoundEntityException(Set<NotFoundEntityException> errors) {
    super("multiple", createErrorMessage(errors));
  }

  private static String createErrorMessage(Set<NotFoundEntityException> errors) {
    if (errors.size() == 1) {
      return errors.iterator().next().getMessage();
    } else {
      return errors.stream().map(Throwable::getMessage).collect(Collectors.joining("|"));
    }
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
