package io.cmartinezs.authboot.infra.exception;

public class EmailServiceException extends RuntimeException {
  public EmailServiceException(String message) {
    super(message);
  }
}
