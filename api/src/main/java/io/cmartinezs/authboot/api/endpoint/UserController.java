package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.request.user.PatchEditUserRequest;
import io.cmartinezs.authboot.api.request.user.PostCreateUserRequest;
import io.cmartinezs.authboot.api.response.UserPostCreateSuccess;
import io.cmartinezs.authboot.api.response.UserResponse;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.core.command.user.CreateUserCmd;
import io.cmartinezs.authboot.core.command.user.DeleteUserCmd;
import io.cmartinezs.authboot.core.command.user.GetUserCmd;
import io.cmartinezs.authboot.core.command.user.UpdateUserCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * This class represents a controller to handle user requests.
 * It contains a create user endpoint and an edit user endpoint.
 * <p>
 * The create user endpoint receives a create user request and returns a user id.
 * The edit user endpoint receives an edit user request and returns the edited user.
 * Both endpoints return a response entity with a base response.
 * The base response contains a success message and a data object.
 * </p>
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServicePort userService;

    /**
     * This method handles a create user request.
     * It receives a create user request and returns a user id.
     * <p>
     * The create user request contains a username, password, email and roles.
     * The username, password, email and roles are used to create a user.
     * If the creation is successful, the user id is returned.
     * The response contains a base response with a success message and a data object.
     * </p>
     *
     * @param request The create user request.
     * @return A response entity with a base response.
     */
    @PostMapping
    @PreAuthorize("hasRole('APP_ADM_C')")
    public ResponseEntity<BaseResponse> createUser(@RequestBody @Validated PostCreateUserRequest request) {
        Integer userId = userService.createUser(toCmd(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(userId));
    }

    /**
     * This method handles a get user request.
     * It receives a get user request and returns a user.
     * <p>
     * The get user request contains a username.
     * The username is used to get a user.
     * If the retrieval is successful, the user is returned.
     * The response contains a base response with a success message and a data object.
     * The data object contains the user.
     * If the retrieval is not successful, an exception is thrown.
     * The exception is handled by the exception handler.
     * The exception handler returns a response entity with a base response.
     * </p>
     *
     * @param username The username.
     * @return A response entity with a base response.
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('APP_ADM_R', 'APP_FEAT_R')")
    public ResponseEntity<BaseResponse> getUser(@PathVariable String username) {
        User user = userService.getUser(new GetUserCmd(username));
        return ResponseEntity.ok(toResponse(user, "G00", "Success user retrieval"));
    }

    /**
     * This method handles an edit user request.
     * It receives an edit user request and returns the edited user.
     * <p>
     * The edit user request contains a username, old password, new password, email and roles.
     * The username, old password, new password, email and roles are used to edit a user.
     * If the edition is successful, the edited user is returned.
     * The response contains a base response with a success message and a data object.
     * The data object contains the edited user.
     * If the edition is not successful, an exception is thrown.
     * The exception is handled by the exception handler.
     * The exception handler returns a response entity with a base response.
     * </p>
     *
     * @param request The edit user request.
     * @return A response entity with a base response.
     */
    @PatchMapping
    @PreAuthorize("hasAnyRole('APP_ADM_U', 'APP_FEAT_U')")
    public ResponseEntity<BaseResponse> editUser(@RequestBody @Validated PatchEditUserRequest request) {
        User editedUser = userService.updateUser(toCmd(request));
        return ResponseEntity.ok(toResponse(editedUser, "U00", "Success user edition"));
    }

    /**
     * This method handles a delete user request.
     * It receives a delete user request and returns the deleted user.
     * <p>
     * The delete user request contains a username.
     * The username is used to delete a user.
     * If the deletion is successful, the deleted user is returned.
     * The response contains a base response with a success message and a data object.
     * The data object contains the deleted user.
     * If the deletion is not successful, an exception is thrown.
     * The exception is handled by the exception handler.
     * The exception handler returns a response entity with a base response.
     * </p>
     *
     * @param username The username.
     * @return A response entity with a base response.
     */
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('APP_ADM_D')")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable String username) {
        User user = userService.deleteUser(new DeleteUserCmd(username));
        return ResponseEntity.ok(toResponse(user, "D00", "Success user deletion"));
    }

    private BaseResponse toResponse(User user, String code, String message) {
        return BaseResponse.builder()
                .success(new MessageResponse(code, message))
                .data(toResponse(user))
                .build();
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(user.getAuthorities())
                .build();
    }

    private BaseResponse toResponse(Integer userId) {
        return BaseResponse.builder()
                .success(new MessageResponse("C00", "Success user creation"))
                .data(new UserPostCreateSuccess(userId))
                .build();
    }

    private CreateUserCmd toCmd(PostCreateUserRequest request) {
        return new CreateUserCmd(request.getUsername(), request.getPassword(), request.getEmail(), request.getRoles());
    }

    private UpdateUserCmd toCmd(PatchEditUserRequest request) {
        return UpdateUserCmd.builder()
                .username(request.getUsername())
                .oldPassword(request.getOldPassword())
                .newPassword(request.getNewPassword())
                .email(request.getEmail())
                .roles(request.getRoles())
                .build();
    }
}
