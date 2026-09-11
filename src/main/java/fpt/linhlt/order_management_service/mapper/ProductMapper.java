package fpt.linhlt.order_management_service.mapper;

import fpt.linhlt.order_management_service.dto.response.ProductResponse;
import fpt.linhlt.order_management_service.dto.response.ProductVariantResponse;
import fpt.linhlt.order_management_service.entity.Product;
import fpt.linhlt.order_management_service.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toProductResponse(Product product);
    List<ProductResponse> toListProductResponse(List<Product> products);
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);
}
