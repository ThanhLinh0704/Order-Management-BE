package fpt.linhlt.order_management_service.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {
    private String id;
    private String productId;
    private String productName;
    private String sku;
    private String color;
    private String size;
    private BigDecimal price;
}
