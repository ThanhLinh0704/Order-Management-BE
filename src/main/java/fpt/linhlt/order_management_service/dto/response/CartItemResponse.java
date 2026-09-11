package fpt.linhlt.order_management_service.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private String cartItemId;
    private String productVariantName;
    private String sku;
    private String color;
    private String size;
    private BigDecimal price;
    private Integer quantity;
}
