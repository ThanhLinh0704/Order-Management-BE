package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.response.CartResponse;

public interface CartService {
    CartResponse getCartByUserId(Long userId);
}
