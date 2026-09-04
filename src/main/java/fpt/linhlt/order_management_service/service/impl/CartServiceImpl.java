package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.AddToCartRequest;
import fpt.linhlt.order_management_service.dto.response.CartItemResponse;
import fpt.linhlt.order_management_service.entity.Cart;
import fpt.linhlt.order_management_service.entity.CartItem;
import fpt.linhlt.order_management_service.entity.ProductVariant;
import fpt.linhlt.order_management_service.repository.CartItemRepository;
import fpt.linhlt.order_management_service.repository.CartRepository;
import fpt.linhlt.order_management_service.repository.ProductVariantRepository;
import fpt.linhlt.order_management_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    public List<CartItemResponse> addtoCart(AddToCartRequest request) {

        Cart cart = cartRepository.findByUserId(1L)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                                .userId(1L)
                                .items(new ArrayList<>())
                        .build()));

        ProductVariant variant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(()-> new RuntimeException("ko co"));
        CartItem newItem = CartItem.builder()
                .cart(cart)
                .productVariant(variant)
                .quantity(request.getQuantity())
                .build();

        cart.getItems().add(newItem);
        cartRepository.save(cart);
        return getCartItems();
    }

    @Override
    public List<CartItemResponse> getCartItems() {

        List<CartItem> items = cartItemRepository.findAll();
        List<CartItemResponse> responses = new ArrayList<>();

        for (CartItem item: items) {
            ProductVariant variant = item.getProductVariant();
            CartItemResponse itemResponse = CartItemResponse.builder()
                    .cartItemId(item.getId())
                    .quantity(item.getQuantity())
                    .sku(variant.getSku())
                    .size(variant.getSize())
                    .color(variant.getColor())
                    .price(variant.getPrice())
                    .productVariantName(variant.getProduct().getName())
                    .build();

            responses.add(itemResponse);
        }
       return responses;
    }
}
