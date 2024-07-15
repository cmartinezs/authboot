package io.cmartinezs.authboot.infra.adapter.service.email;

import java.util.Map;

public interface EmailStrategy {
  String getConfigName();

  Map<String, String> getVariables();

  Map<String, Map<String, Map<String, String>>> getUris();
}
