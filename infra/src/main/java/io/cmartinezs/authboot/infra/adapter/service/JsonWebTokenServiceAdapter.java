package io.cmartinezs.authboot.infra.adapter.service;

import io.cmartinezs.authboot.commons.DateUtils;
import io.cmartinezs.authboot.core.command.auth.GenerateTokenCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.infra.utils.properties.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * This class is an adapter for the Json Web Token library.
 *
 * <p>This class is responsible for generating a token based on the properties defined in the
 * application.yml file.
 *
 * <p>The token is generated using the HS512 algorithm and the secret is defined in the
 * application.yml file.
 *
 * <p>The token is generated with the following claims:
 *
 * <p>- authorities: The authorities of the user. - issuer: The issuer of the token. - subject: The
 * subject of the token. - issuedAt: The date when the token was issued. - expiration: The date when
 * the token will expire.
 */
@RequiredArgsConstructor
public class JsonWebTokenServiceAdapter implements TokenServicePort {

  private static final Map<String, Function<String, byte[]>> DECODING_FUNCTIONS;

  static {
    DECODING_FUNCTIONS = new HashMap<>();
    DECODING_FUNCTIONS.put("BASE64", Decoders.BASE64::decode);
    DECODING_FUNCTIONS.put("BASE64_URL", Decoders.BASE64URL::decode);
    DECODING_FUNCTIONS.put("NONE", secretString -> secretString.getBytes(StandardCharsets.UTF_8));
  }

  private final TokenProperties tokenProperties;

  /**
   * This method generates a key based on the secret defined in the application.yml file.
   *
   * @return The key generated.
   */
  private Key generateKey() {
    return Keys.hmacShaKeyFor(getBytesByEncodedType());
  }

  /**
   * This method returns the secret in bytes based on the encoding type defined in the
   * application.yml file.
   *
   * @return The secret in bytes.
   */
  private byte[] getBytesByEncodedType() {
    return DECODING_FUNCTIONS
        .getOrDefault(tokenProperties.getSecretEncodingType(), DECODING_FUNCTIONS.get("NONE"))
        .apply(tokenProperties.getSecret());
  }

  /**
   * This method calculates the expiration date of the token based on the expiration time defined in
   * the application.yml file.
   *
   * @return The expiration date of the token.
   */
  private LocalDateTime calculateExpirationDate() {
    return LocalDateTime.now().plusSeconds(tokenProperties.getExpiration());
  }

  /**
   * This method generates a token based on the properties defined in the application.yml file.
   *
   * @param generateTokenCmd The command with the information to generate the token.
   * @return The token generated.
   */
  @Override
  public String generate(GenerateTokenCmd generateTokenCmd) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("authorities", generateTokenCmd.getAuthorities());
    return Jwts.builder()
        .setClaims(claims)
        .setIssuer(tokenProperties.getIssuer())
        .setSubject(generateTokenCmd.getUsername())
        .setIssuedAt(DateUtils.toDate(LocalDateTime.now()))
        .setExpiration(DateUtils.toDate(calculateExpirationDate()))
        .signWith(generateKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  @Override
  public Optional<String> getUsername(String token) {
    return Optional.ofNullable(getClaimFromToken(token, Claims::getSubject));
  }

  @Override
  public boolean validate(String token, User user) {
    final var username = getUsername(token);
    if (username.isEmpty() || !username.get().equals(user.getUsername())) {
      return false;
    }

    final LocalDateTime created = getIssuedAtDateFromToken(token);
    return !isTokenExpired(token)
        && isCreatedBeforeNextPasswordReset(created, user.getPasswordResetAt());
  }

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(getAllClaimsFromToken(token));
  }

  private LocalDateTime getIssuedAtDateFromToken(String token) {
    var dateFromToken = getClaimFromToken(token, Claims::getIssuedAt);
    return DateUtils.toLocalDateTime(dateFromToken);
  }

  private LocalDateTime getExpirationDateFromToken(String token) {
    var claimFromToken = getClaimFromToken(token, Claims::getExpiration);
    return DateUtils.toLocalDateTime(claimFromToken);
  }

  private Boolean isTokenExpired(String token) {
    final LocalDateTime expirationDate = getExpirationDateFromToken(token);
    return expirationDate.isBefore(LocalDateTime.now());
  }

  private Boolean isCreatedBeforeNextPasswordReset(
      LocalDateTime created, LocalDateTime lastPasswordReset) {
    return (lastPasswordReset == null || created.isBefore(lastPasswordReset));
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(generateKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
