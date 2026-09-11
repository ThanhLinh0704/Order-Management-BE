package fpt.linhlt.order_management_service.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

import fpt.linhlt.order_management_service.common.InventoryStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private String cartItemId;
    private String productVariantName;
    private String sku;
    private Map<String, Object> attributes;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    private InventoryStatus inventoryStatus;
}
