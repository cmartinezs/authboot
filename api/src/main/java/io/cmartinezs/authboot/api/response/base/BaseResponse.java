package io.cmartinezs.authboot.api.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a base response.
 *
 * <p>This class is extended by all the responses. It contains the date of the response, a success
 * message, a failure message and a debug message. The debug message is only shown if the
 * application is running in debug mode. The throwable is only shown if the application is running
 * in debug mode. The debug message and the throwable are not shown in the production environment.
 * The success message and the failure message are shown in the production environment.
 */
@Getter
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
  @Builder.Default private final LocalDateTime date = LocalDateTime.now();
  private MessageResponse success;
  private MessageResponse failure;
  private Object data;
  @Setter private MessageResponse debug;
  @Setter private String throwable;
}
