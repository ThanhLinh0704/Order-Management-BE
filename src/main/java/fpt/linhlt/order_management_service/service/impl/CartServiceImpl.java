package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.common.InventoryStatus;
import fpt.linhlt.order_management_service.dto.request.AddToCartRequest;
import fpt.linhlt.order_management_service.dto.request.UpdateCartItemQuantityRequest;
import fpt.linhlt.order_management_service.dto.response.CartItemResponse;
import fpt.linhlt.order_management_service.dto.response.CartResponse;
import fpt.linhlt.order_management_service.entity.*;
import fpt.linhlt.order_management_service.mapper.CartItemMapper;
import fpt.linhlt.order_management_service.repository.*;
import fpt.linhlt.order_management_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final CartItemMapper cartItemMapper;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập");
        }
        return userRepository.findById(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng"));
    }

    @Override
    @Transactional
    public CartItemResponse addToCart(AddToCartRequest request) {
        User user = getCurrentUser();
        ProductVariant variant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy biến thể sản phẩm"));

        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        CartItem cartItem = cartItemRepository
                .findByCart_IdAndProductVariant_Id(cart.getId(), variant.getId())
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductVariant(variant);
            cartItem.setQuantity(request.getQuantity());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }
        cartItem = cartItemRepository.save(cartItem);

        return mapToCartItemResponse(cartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);

        if (cart == null) {
            return CartResponse.builder()
                    .items(List.of())
                    .subtotal(BigDecimal.ZERO)
                    .discountRate(BigDecimal.ZERO)
                    .discount(BigDecimal.ZERO)
                    .shippingFee(BigDecimal.ZERO)
                    .totalAmount(BigDecimal.ZERO)
                    .build();
        }

        List<CartItem> cartItems = cartItemRepository.findAllByCart_Id(cart.getId());
        List<CartItemResponse> itemResponses = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            CartItemResponse response = mapToCartItemResponse(item);
            itemResponses.add(response);
            subtotal = subtotal.add(response.getTotalPrice());
        }

        BigDecimal discountRate = new BigDecimal("0.05");
        BigDecimal discount = subtotal.multiply(discountRate);
        BigDecimal shippingFee = cartItems.isEmpty() ? BigDecimal.ZERO : new BigDecimal("145.00");
        BigDecimal totalAmount = subtotal.subtract(discount).add(shippingFee);

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(user.getId())
                .items(itemResponses)
                .subtotal(subtotal)
                .discountRate(discountRate)
                .discount(discount)
                .shippingFee(shippingFee)
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    @Transactional
    public CartResponse updateQuantity(String cartItemId, UpdateCartItemQuantityRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy sản phẩm trong giỏ hàng"));
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        return getCart();
    }

    @Override
    @Transactional
    public void removeItem(String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy sản phẩm trong giỏ hàng"));
        cartItemRepository.delete(cartItem);
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        ProductVariant variant = cartItem.getProductVariant();
        CartItemResponse response = cartItemMapper.toCartItemResponse(cartItem);

        BigDecimal price = variant.getPrice();
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        response.setTotalPrice(totalPrice);

        InventoryItem item = inventoryItemRepository.findByProductVariant_Id(variant.getId()).orElse(null);

        int stockInWarehouse = (item != null) ? item.getQuantityInStock() : 0;

        if (stockInWarehouse <= 0) {
            response.setInventoryStatus(InventoryStatus.OUT_OF_STOCK);
        } else if (stockInWarehouse < cartItem.getQuantity() || stockInWarehouse <= 5) {
            response.setInventoryStatus(InventoryStatus.LIMITED_STOCK);
        } else {
            response.setInventoryStatus(InventoryStatus.IN_STOCK);
        }

        return response;
    }
}
