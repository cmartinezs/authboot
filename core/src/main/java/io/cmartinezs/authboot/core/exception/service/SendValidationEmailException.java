package io.cmartinezs.authboot.core.exception.service;

public class SendValidationEmailException extends RuntimeException {
  public SendValidationEmailException(String email, String username) {
    super(
        String.format(
            "An error occurred while sending validation to email %s. User %s was not created.",
            email, username));
  }

  @Override
  public String toString() {
    return getMessage();
  }
}
