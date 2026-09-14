package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.request.CreateUserRequest;
import fpt.linhlt.order_management_service.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getMyInformation();
}
