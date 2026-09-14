package fpt.linhlt.order_management_service.controller;

import fpt.linhlt.order_management_service.dto.request.ApiResponse;
import fpt.linhlt.order_management_service.dto.request.CreateUserRequest;
import fpt.linhlt.order_management_service.dto.response.UserResponse;
import fpt.linhlt.order_management_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping("/myInformation")
    public ApiResponse<UserResponse> getMyInformation() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInformation())
                .build();
    }
}
