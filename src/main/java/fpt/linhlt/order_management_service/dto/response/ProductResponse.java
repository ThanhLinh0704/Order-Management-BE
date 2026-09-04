package fpt.linhlt.order_management_service.dto.response;

import java.util.List;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private List<ProductResponse> variants;
}
