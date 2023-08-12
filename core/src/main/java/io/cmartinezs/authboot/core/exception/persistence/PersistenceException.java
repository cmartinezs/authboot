package io.cmartinezs.authboot.core.exception.persistence;

import lombok.Getter;

@Getter
public abstract class PersistenceException extends RuntimeException {
  private final String entityName;

  public PersistenceException(String entityName, String message) {
    super(message);
    this.entityName = entityName;
  }
}
