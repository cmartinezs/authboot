package io.cmartinezs.authboot.core.exception;

public class PasswordNotMatchException extends RuntimeException {

  public static final String MSG = "The current password does not match";

  public PasswordNotMatchException() {
    super(MSG);
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
