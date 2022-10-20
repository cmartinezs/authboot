package io.cmartinezs.authboot.api.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@SuperBuilder
public class JwtLoginSuccess extends BaseResponse {
  private final String token;
}
