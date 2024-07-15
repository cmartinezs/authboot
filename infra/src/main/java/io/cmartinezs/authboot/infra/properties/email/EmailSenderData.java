package io.cmartinezs.authboot.infra.properties.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSenderData {
  String from;
  String personal;
  String subject;
}
