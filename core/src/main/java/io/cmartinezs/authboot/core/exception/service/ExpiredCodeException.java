package io.cmartinezs.authboot.core.exception.service;

public class ExpiredCodeException extends RuntimeException {
  public ExpiredCodeException(String codeName) {
    super(String.format("The %s code has expired.", codeName));
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
