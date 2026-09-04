package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.request.AddToCartRequest;
import fpt.linhlt.order_management_service.dto.response.CartItemResponse;

import java.util.List;

public interface CartService {
    List<CartItemResponse> addtoCart(AddToCartRequest request);
    List<CartItemResponse> getCartItems();
}
