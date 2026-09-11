package fpt.linhlt.order_management_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {
    private String id;
    private String productId;
    private String productName;
    private String sku;
    private String variantName;
    private Map<String, Object> attributes;
    private BigDecimal price;
}
