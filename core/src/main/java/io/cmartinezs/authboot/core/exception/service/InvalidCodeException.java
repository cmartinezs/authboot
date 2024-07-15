package io.cmartinezs.authboot.core.exception.service;

public class InvalidCodeException extends RuntimeException {
  public InvalidCodeException(String codeName) {
    super(String.format("The %s code is invalid.", codeName));
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
