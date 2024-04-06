package io.cmartinezs.authboot.infra.properties.email;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDataData {
  @NotNull @Valid EmailTemplateData template;
  @NotNull @Valid EmailSenderData sender;
}
