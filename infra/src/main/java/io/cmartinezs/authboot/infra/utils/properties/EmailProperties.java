package io.cmartinezs.authboot.infra.utils.properties;

import io.cmartinezs.authboot.core.utils.property.EmailSenderInfo;
import io.cmartinezs.authboot.infra.properties.UriProperties;

import java.util.Map;

public interface EmailProperties {
    Map<String, EmailSenderInfo> getSenderInfo();
    Map<String, UriProperties> getUris();
}
