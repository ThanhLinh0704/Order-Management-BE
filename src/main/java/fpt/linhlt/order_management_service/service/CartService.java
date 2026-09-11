package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.request.AddToCartRequest;
import fpt.linhlt.order_management_service.dto.request.UpdateCartItemQuantityRequest;
import fpt.linhlt.order_management_service.dto.response.CartItemResponse;
import fpt.linhlt.order_management_service.dto.response.CartResponse;

public interface CartService {
    CartItemResponse addToCart(AddToCartRequest request);

    CartResponse getCart();

    CartResponse updateQuantity(String cartItemId, UpdateCartItemQuantityRequest request);

    void removeItem(String cartItemId);
}
