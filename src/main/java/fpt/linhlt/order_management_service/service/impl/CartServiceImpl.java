package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.AddToCartRequest;
import fpt.linhlt.order_management_service.dto.response.CartItemResponse;
import fpt.linhlt.order_management_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @Override
    public List<CartItemResponse> addtoCart(AddToCartRequest request) {
        return List.of();
    }

    @Override
    public List<CartItemResponse> getCartItems() {
        return List.of();
    }
}
