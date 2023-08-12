package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.request.role.PostCreateRoleRequest;
import io.cmartinezs.authboot.api.response.RoleCreateSuccess;
import io.cmartinezs.authboot.api.response.RoleGetAllSuccess;
import io.cmartinezs.authboot.api.response.RoleGetByCodeSuccess;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.core.command.role.CreateRoleCmd;
import io.cmartinezs.authboot.core.command.role.GetRoleByCodeCmd;
import io.cmartinezs.authboot.core.entity.domain.user.Role;
import io.cmartinezs.authboot.core.port.service.RoleServicePort;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * This class is a controller for the role endpoints.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServicePort roleService;

    /**
     * This method returns all the roles.
     *
     * @return a ResponseEntity with the roles.
     */
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

    /**
     * This method returns a role by code.
     *
     * @param code the code of the role.
     * @return a ResponseEntity with the role.
     */
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('APP_ADM_R')")
    public ResponseEntity<BaseResponse> getByCode(@PathVariable String code) {
        Role role = roleService.getByCode(new GetRoleByCodeCmd(code));
        return ResponseEntity.ok(toResponse(role));
    }

    private BaseResponse toResponse(Role role) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success role retrieval"))
                .data(new RoleGetByCodeSuccess(role))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('APP_ADM_C')")
    public ResponseEntity<BaseResponse> createRole(@RequestBody PostCreateRoleRequest request) {
        var roleId = roleService.createRole(toCmd(request));
        return ResponseEntity.ok(toResponse(roleId));
    }

    private BaseResponse toResponse(Integer roleId) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success role creation"))
                .data(new RoleCreateSuccess(roleId))
                .build();
    }

    private CreateRoleCmd toCmd(PostCreateRoleRequest request) {
        return new CreateRoleCmd(request.getCode(), request.getName(), request.getDescription(), request.getPermissions());
    }

}
