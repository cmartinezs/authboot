package io.cmartinezs.authboot.api.endpoint;

import io.cmartinezs.authboot.api.request.PostCreateUserRequest;
import io.cmartinezs.authboot.api.response.UserPostCreateSuccess;
import io.cmartinezs.authboot.api.response.base.BaseResponse;
import io.cmartinezs.authboot.api.response.base.MessageResponse;
import io.cmartinezs.authboot.core.command.CreateUserCmd;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServicePort userService;

    @PostMapping
    @PreAuthorize("hasRole('APP_ADM_C')")
    public ResponseEntity<BaseResponse> createUser(@RequestBody @Validated PostCreateUserRequest request) {
        Integer userId = userService.createUser(toCmd(request));
        return ResponseEntity.ok(toResponse(userId));
    }

    private BaseResponse toResponse(Integer userId) {
        return BaseResponse.builder()
                .success(new MessageResponse("S00", "Success user creation"))
                .data(new UserPostCreateSuccess(userId))
                .build();
    }

    private CreateUserCmd toCmd(PostCreateUserRequest request) {
        return new CreateUserCmd(request.getUsername(), request.getPassword(), request.getEmail(), request.getRoles());
    }
}
