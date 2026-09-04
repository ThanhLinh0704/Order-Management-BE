package fpt.linhlt.order_management_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateProductRequest {

    private String name;
    private String desciption;
    private List<CreateProductVariantRequest> variants;
}
