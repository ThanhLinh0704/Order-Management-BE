package fpt.linhlt.order_management_service.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private String cartId;
    private String userId;
    private List<CartItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal discountRate;
    private BigDecimal discount;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
}
