package io.cmartinezs.authboot.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Carlos
 * @version 1.0
 */
@Getter
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
  @Builder.Default private final LocalDateTime date = LocalDateTime.now();
  private MessageResponse success;
  private MessageResponse failure;
  @Setter private MessageResponse debug;
  @Setter private String throwable;
}
