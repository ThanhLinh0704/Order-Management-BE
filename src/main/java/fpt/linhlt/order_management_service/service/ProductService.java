package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.request.CreateProductRequest;
import fpt.linhlt.order_management_service.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProductById(String id);
}
