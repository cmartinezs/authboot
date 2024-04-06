package io.cmartinezs.authboot.infra.properties.email;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailTemplateData {
  @Size(min = 4, max = 255)
  String name;
  @NotEmpty
  List<@Size(min = 3) String> variables;
  @NotEmpty
  Map<String, @Valid EmailLinkData> links;
}
