package fpt.linhlt.order_management_service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductVariantResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String sku;
    private String color;
    private String size;
    private BigDecimal price;
}
