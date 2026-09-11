package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.response.ProductVariantResponse;

public interface ProductVariantService {
    ProductVariantResponse getVariantById(String id);
}
