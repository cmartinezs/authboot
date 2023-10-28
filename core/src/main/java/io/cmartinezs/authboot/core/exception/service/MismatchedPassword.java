package io.cmartinezs.authboot.core.exception.service;

public class MismatchedPassword extends RuntimeException {

  public static final String MSG = "The current password does not match";

  public MismatchedPassword() {
    super(MSG);
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
