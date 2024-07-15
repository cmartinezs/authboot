package io.cmartinezs.authboot.core.exception.service;

public class MismatchedPasswordException extends RuntimeException {
  public MismatchedPasswordException() {
    super("The current password does not match");
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
