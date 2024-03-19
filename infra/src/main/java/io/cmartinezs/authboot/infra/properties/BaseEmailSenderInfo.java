package io.cmartinezs.authboot.infra.properties;

import io.cmartinezs.authboot.core.utils.property.EmailSenderInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEmailSenderInfo implements EmailSenderInfo {
  @Size(min = 5, max = 50)
  private String name;

  @Email private String from;

  @Size(min = 5, max = 50)
  private String personal;

  @Size(min = 5, max = 50)
  private String subject;
}
