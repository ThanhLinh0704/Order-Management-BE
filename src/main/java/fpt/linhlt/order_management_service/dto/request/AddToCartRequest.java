package fpt.linhlt.order_management_service.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    private String productVariantId;

    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
