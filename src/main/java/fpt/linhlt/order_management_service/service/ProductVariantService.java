package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.request.CreateProductVariantRequest;
import fpt.linhlt.order_management_service.dto.response.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {
    ProductVariantResponse createProductVariant(CreateProductVariantRequest request);
    List<ProductVariantResponse> getAllProductVariants();
    ProductVariantResponse getProductVariantById(String productVariantId);
}
