package io.cmartinezs.authboot.api.endpoint.utils;

import io.cmartinezs.authboot.api.request.user.UserPatchEmailValidationRequest;
import io.cmartinezs.authboot.api.request.user.UserPatchPasswordRecoveryRequest;
import io.cmartinezs.authboot.api.request.user.UserPatchRequest;
import io.cmartinezs.authboot.api.request.user.UserPostRequest;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.api.response.user.*;
import io.cmartinezs.authboot.core.command.user.CreateUserCmd;
import io.cmartinezs.authboot.core.command.user.RecoverPasswordCmd;
import io.cmartinezs.authboot.core.command.user.UpdateUserCmd;
import io.cmartinezs.authboot.core.command.user.ValidateUserCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserControllerUtils {

    public static CreateUserCmd toCmd(UserPostRequest request) {
        return new CreateUserCmd(
                request.getUsername(), request.getPassword(), request.getEmail(), request.getRoles());
    }

    public static BaseResponse toPostResponse(Integer userId) {
        return BaseResponse.builder()
                .success(new MessageResponse("C00", "Success user creation"))
                .data(new UserPostResponse(userId))
                .build();
    }

    public static BaseResponse toUserGetByUsernameResponse(User user, String code, String message) {
        return BaseResponse.builder()
                .success(new MessageResponse(code, message))
                .data(new UserGetByUsernameResponse(toUserResponse(user)))
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(user.getAuthorities())
                .build();
    }

    public static UpdateUserCmd toCmd(String username, UserPatchRequest request) {
        return UpdateUserCmd.builder()
                .username(username)
                .oldPassword(request.getOldPassword())
                .newPassword(request.getNewPassword())
                .email(request.getEmail())
                .roles(request.getRoles())
                .build();
    }

    public static RecoverPasswordCmd toCmd(String username, UserPatchPasswordRecoveryRequest request) {
        return new RecoverPasswordCmd(username, request.getRecoveryCode(), request.getPassword());
    }

    public static ValidateUserCmd toCmd(String username, UserPatchEmailValidationRequest request) {
        return new ValidateUserCmd(username, request.getEmail(), request.getValidationCode());
    }

    public static BaseResponse toUserPatchByUsernameResponse(User user, String code, String message) {
        return BaseResponse.builder()
                .success(new MessageResponse(code, message))
                .data(new UserPatchByUsernameResponse(toUserResponse(user)))
                .build();
    }

    public static BaseResponse toUserDeleteByUsernameResponse(User user, String code, String message) {
        return BaseResponse.builder()
                .success(new MessageResponse(code, message))
                .data(new UserDeleteByUsernameResponse(toUserResponse(user)))
                .build();
    }
}
