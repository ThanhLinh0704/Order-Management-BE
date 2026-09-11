package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.request.LoginRequest;
import fpt.linhlt.order_management_service.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}