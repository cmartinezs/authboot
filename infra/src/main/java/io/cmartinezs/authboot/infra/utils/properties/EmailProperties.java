package io.cmartinezs.authboot.infra.utils.properties;

import io.cmartinezs.authboot.infra.properties.EmailTemplateProperties;
import io.cmartinezs.authboot.infra.properties.UriProperties;

import java.util.Map;

public interface EmailProperties {
    Map<String, EmailTemplateProperties> getTemplates();
    Map<String, UriProperties> getUris();
}
