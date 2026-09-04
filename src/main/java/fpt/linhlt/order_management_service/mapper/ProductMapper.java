package fpt.linhlt.order_management_service.mapper;

import fpt.linhlt.order_management_service.dto.response.ProductResponse;
import fpt.linhlt.order_management_service.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);
    List<ProductResponse> toListProductResponse(List<Product> products);
}
