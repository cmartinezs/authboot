package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.response.RoleGetAllSuccess;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.core.entity.domain.user.Role;
import io.cmartinezs.authboot.core.port.service.RoleServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServicePort roleService;

    @GetMapping
    @PreAuthorize("hasRole('APP_ADM_R')")
    public ResponseEntity<BaseResponse> getRoles() {
        Set<Role> roles = roleService.getAll();
        return ResponseEntity.ok(toResponse(roles));
    }

    private BaseResponse toResponse(Set<Role> roles) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success roles retrieval"))
                .data(new RoleGetAllSuccess(roles))
                .build();
    }
}
