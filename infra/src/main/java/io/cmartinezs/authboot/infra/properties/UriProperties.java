package io.cmartinezs.authboot.infra.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UriProperties {
    private String scheme;
    private String host;
    private String port;
    private String path;
}
