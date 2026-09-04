package fpt.linhlt.order_management_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductVariantRequest {

    private String sku;
    private String color;
    private String size;
    private BigDecimal price;
}
