package io.cmartinezs.authboot.api.response.role;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleResponse {
    private String code;
    private String name;
    private String description;
    private Set<String> permissions;
}
