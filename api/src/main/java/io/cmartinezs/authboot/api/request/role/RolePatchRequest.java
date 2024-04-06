package io.cmartinezs.authboot.api.request.role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RolePatchRequest {
    private String name;
    private String description;
    @Valid
    private List<@Valid Permission> permissions;
    @Getter
    @Setter
    @Builder
    public static class Permission {
        @NotBlank
        private String code;
        @NotEmpty
        private List<@Valid @NotBlank String> types;
    }
}
