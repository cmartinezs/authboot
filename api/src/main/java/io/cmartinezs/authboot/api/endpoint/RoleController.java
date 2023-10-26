package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.request.role.RolePatchRequest;
import io.cmartinezs.authboot.api.request.role.RolePostRequest;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.api.response.role.RoleGetAllSuccessResponse;
import io.cmartinezs.authboot.api.response.role.RolePostSuccessResponse;
import io.cmartinezs.authboot.api.response.role.RoleResponse;
import io.cmartinezs.authboot.core.command.role.CreateRoleCmd;
import io.cmartinezs.authboot.core.command.role.RoleDeleteCmd;
import io.cmartinezs.authboot.core.command.role.GetRoleByCodeCmd;
import io.cmartinezs.authboot.core.command.role.RoleUpdateCmd;
import io.cmartinezs.authboot.core.entity.domain.user.Function;
import io.cmartinezs.authboot.core.entity.domain.user.Role;
import io.cmartinezs.authboot.core.port.service.RoleServicePort;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * This class is a controller for the role endpoints.
 */
@RestController
@RequestMapping("/roles")
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
        return ResponseEntity.ok(toGetByCodeResponse(roles));
    }

    private BaseResponse toGetByCodeResponse(Set<Role> roles) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success roles retrieval"))
                .data(getAllSuccess(roles))
                .build();
    }

    private static RoleGetAllSuccessResponse getAllSuccess(Set<Role> roles) {
        return new RoleGetAllSuccessResponse(roles.stream()
                .map(RoleController::toRoleResponse)
                .collect(Collectors.toSet()));
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
        return ResponseEntity.ok(toGetByCodeResponse(role));
    }

    private BaseResponse toGetByCodeResponse(Role role) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success role retrieval"))
                .data(toRoleResponse(role))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('APP_ADM_C')")
    public ResponseEntity<BaseResponse> createRole(@RequestBody RolePostRequest request) {
        var roleId = roleService.createRole(toCmd(request));
        return ResponseEntity.ok(toGetByCodeResponse(roleId));
    }

    private CreateRoleCmd toCmd(RolePostRequest request) {
        return new CreateRoleCmd(request.getCode(), request.getName(), request.getDescription(), request.getPermissions());
    }

    private BaseResponse toGetByCodeResponse(Integer roleId) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success role creation"))
                .data(new RolePostSuccessResponse(roleId))
                .build();
    }

    @PatchMapping("/{code}")
    @PreAuthorize("hasRole('APP_ADM_U')")
    public ResponseEntity<BaseResponse> updateRole(@PathVariable String code, @RequestBody RolePatchRequest request) {
        var role = roleService.updateRole(toCmd(code, request));
        return ResponseEntity.ok(toUpdateResponse(role));
    }

    private BaseResponse toUpdateResponse(Role role) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success role update"))
                .data(toRoleResponse(role))
                .build();
    }

    private static RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .code(role.getCode())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(toPermissions(role))
                .build();
    }

    private static Set<String> toPermissions(Role role) {
        return role.getFunctions().stream().map(Function::getCode).collect(Collectors.toSet());
    }

    private RoleUpdateCmd toCmd(String code, RolePatchRequest request) {
        return RoleUpdateCmd.builder()
                .code(code)
                .name(request.getName())
                .description(request.getDescription())
                .permissions(request.getPermissions())
                .build();
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('APP_ADM_D')")
    public ResponseEntity<BaseResponse> deleteRole(@PathVariable String code) {
        var role = roleService.deleteRole(new RoleDeleteCmd(code));
        return ResponseEntity.ok(toDeleteResponse(role));
    }

    private BaseResponse toDeleteResponse(Role role) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success role deletion"))
                .data(toRoleResponse(role))
                .build();
    }
}
