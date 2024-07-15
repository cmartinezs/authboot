package io.cmartinezs.authboot.core.port.service;

/** This interface is used to define the password encoder service. */
public interface PasswordEncoderServicePort {
  /**
   * This method is used to compare the old password with the new password.
   *
   * @param oldPassword The old password.
   * @param password The new password.
   * @return true if the passwords match, false otherwise.
   */
  boolean matches(String oldPassword, String password);

  /**
   * This method is used to encrypt the password.
   *
   * @param password The password to encrypt.
   * @return The encrypted password.
   */
  String encrypt(String password);
}
