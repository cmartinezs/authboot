package io.cmartinezs.authboot.api.endpoint;

import static java.util.stream.Collectors.toMap;

import io.cmartinezs.authboot.api.request.role.RolePatchRequest;
import io.cmartinezs.authboot.api.request.role.RolePostRequest;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.api.response.role.*;
import io.cmartinezs.authboot.core.command.role.CreateRoleCmd;
import io.cmartinezs.authboot.core.command.role.GetRoleByCodeCmd;
import io.cmartinezs.authboot.core.command.role.RoleDeleteCmd;
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

/** This class is a controller for the role endpoints. */
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
  public ResponseEntity<BaseResponse> get() {
    return ResponseEntity.ok(toGetResponse(roleService.getAll()));
  }

  private static BaseResponse toGetResponse(Set<Role> roles) {
    return BaseResponse.builder()
        .success(new MessageResponse("S00", "Success roles retrieval"))
        .data(new RolesGetResponse(toSetRoleResponse(roles)))
        .build();
  }

  private static Set<RoleResponse> toSetRoleResponse(Set<Role> roles) {
    return roles.stream().map(RoleController::toRoleResponse).collect(Collectors.toSet());
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

  /**
   * This method returns a role by code.
   *
   * @param code the code of the role.
   * @return a ResponseEntity with the role.
   */
  @GetMapping("/by-code/{code}")
  @PreAuthorize("hasRole('APP_ADM_R')")
  public ResponseEntity<BaseResponse> getByCode(@PathVariable String code) {
    Role role = roleService.getByCode(new GetRoleByCodeCmd(code));
    return ResponseEntity.ok(toGetByCodeResponse(role));
  }

  private static BaseResponse toGetByCodeResponse(Role role) {
    return BaseResponse.builder()
        .success(new MessageResponse("S00", "Success role retrieval"))
        .data(new RoleGetByCodeResponse(toRoleResponse(role)))
        .build();
  }

  @PostMapping
  @PreAuthorize("hasRole('APP_ADM_C')")
  public ResponseEntity<BaseResponse> post(@RequestBody RolePostRequest request) {
    var roleId = roleService.createRole(toCmd(request));
    return ResponseEntity.ok(toPostResponse(roleId));
  }

  private static CreateRoleCmd toCmd(RolePostRequest request) {
    return new CreateRoleCmd(
        request.getCode(),
        request.getName(),
        request.getDescription(),
        request.getPermissions().stream()
            .collect(
                toMap(RolePostRequest.Permission::getCode, RolePostRequest.Permission::getTypes)));
  }

  private static BaseResponse toPostResponse(Integer roleId) {
    return BaseResponse.builder()
        .success(new MessageResponse("S00", "Success role creation"))
        .data(new RolePostResponse(roleId))
        .build();
  }

  @PatchMapping("/by-code/{code}")
  @PreAuthorize("hasRole('APP_ADM_U')")
  public ResponseEntity<BaseResponse> patchByCode(
      @PathVariable String code, @RequestBody RolePatchRequest request) {
    var role = roleService.updateRole(toCmd(code, request));
    return ResponseEntity.ok(toPatchByCodeResponse(role));
  }

  private RoleUpdateCmd toCmd(String code, RolePatchRequest request) {
    return RoleUpdateCmd.builder()
        .code(code)
        .name(request.getName())
        .description(request.getDescription())
        .permissions(
            request.getPermissions().stream()
                .collect(
                    toMap(
                        RolePatchRequest.Permission::getCode,
                        RolePatchRequest.Permission::getTypes)))
        .build();
  }

  private BaseResponse toPatchByCodeResponse(Role role) {
    return BaseResponse.builder()
        .success(new MessageResponse("S00", "Success role update"))
        .data(new RolePatchByCodeResponse(toRoleResponse(role)))
        .build();
  }

  @DeleteMapping("/by-code/{code}")
  @PreAuthorize("hasRole('APP_ADM_D')")
  public ResponseEntity<BaseResponse> deleteRole(@PathVariable String code) {
    var role = roleService.deleteRole(new RoleDeleteCmd(code));
    return ResponseEntity.ok(toDeleteByCodeResponse(role));
  }

  private BaseResponse toDeleteByCodeResponse(Role role) {
    return BaseResponse.builder()
        .success(new MessageResponse("S00", "Success role deletion"))
        .data(new RoleDeleteByCodeResponse(toRoleResponse(role)))
        .build();
  }
}
