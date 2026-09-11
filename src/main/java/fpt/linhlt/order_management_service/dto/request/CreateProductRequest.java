package fpt.linhlt.order_management_service.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    private String name;
    private String description;
    private List<CreateProductVariantRequest> variants;
}
