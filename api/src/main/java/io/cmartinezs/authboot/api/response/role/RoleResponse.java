package io.cmartinezs.authboot.api.response.role;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class RoleResponse {
    private String code;
    private String name;
    private String description;
    private Set<String> permissions;
}
