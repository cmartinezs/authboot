package io.cmartinezs.authboot.core.properties;

/**
 * @author Carlos
 * @version 1.0
 */
public interface TokenProperties {
  String getSecret();

  long getExpiration();

  String getSecretEncodingType();

  String getIssuer();
}
