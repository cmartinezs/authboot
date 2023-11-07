package io.cmartinezs.authboot.infra.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailTemplateProperties {
  private String name;
  private String from;
  private String personal;
  private String subject;
}
