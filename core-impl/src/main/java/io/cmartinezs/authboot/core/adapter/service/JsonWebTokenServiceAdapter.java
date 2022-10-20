package io.cmartinezs.authboot.core.adapter.service;

import io.cmartinezs.authboot.commons.DateUtils;
import io.cmartinezs.authboot.core.command.JwtGenerateCmd;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.core.properties.TokenProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

/**
 * @author Carlos
 * @version 1.0
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

  private Key generateKey() {
    return Keys.hmacShaKeyFor(getBytesByEncodedType());
  }

  private byte[] getBytesByEncodedType() {
    return DECODING_FUNCTIONS
        .getOrDefault(tokenProperties.getSecretEncodingType(), DECODING_FUNCTIONS.get("NONE"))
        .apply(tokenProperties.getSecret());
  }

  private LocalDateTime calculateExpirationDate() {
    return LocalDateTime.now().plusSeconds(tokenProperties.getExpiration());
  }

  @Override
  public String generate(JwtGenerateCmd jwtGenerateCmd) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("authorities", jwtGenerateCmd.authorities());
    return Jwts.builder()
        .setClaims(claims)
        .setIssuer(tokenProperties.getIssuer())
        .setSubject(jwtGenerateCmd.username())
        .setIssuedAt(DateUtils.toDate(LocalDateTime.now()))
        .setExpiration(DateUtils.toDate(calculateExpirationDate()))
        .signWith(generateKey(), SignatureAlgorithm.HS512)
        .compact();
  }
}
