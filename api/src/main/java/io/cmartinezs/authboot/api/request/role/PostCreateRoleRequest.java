package io.cmartinezs.authboot.api.request.role;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostCreateRoleRequest {
    @NotBlank
    private String code;
    @NotBlank
    private String name;
    private String description;
    @Valid
    private Map<@NotBlank String, @NotEmpty @Valid List<@NotBlank String>> permissions;
}
