package fpt.linhlt.order_management_service.controller;

import fpt.linhlt.order_management_service.dto.request.AddToCartRequest;
import fpt.linhlt.order_management_service.dto.request.UpdateCartItemQuantityRequest;
import fpt.linhlt.order_management_service.dto.response.CartItemResponse;
import fpt.linhlt.order_management_service.dto.response.CartResponse;
import fpt.linhlt.order_management_service.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateQuantity(@PathVariable String cartItemId,
            @Valid @RequestBody UpdateCartItemQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateQuantity(cartItemId, request));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable String cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
