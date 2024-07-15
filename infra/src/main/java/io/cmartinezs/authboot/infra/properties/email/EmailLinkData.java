package io.cmartinezs.authboot.infra.properties.email;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailLinkData {
  @Size(min = 4, max = 20)
  private String variableName;
  @Size(min = 4, max = 5)
  private String schema;
  @Size(min = 4, max = 255)
  private String host;
  @Min(1000)
  @Max(65535)
  private Integer port;
  @Size(min = 4, max = 255)
  private String path;
}
