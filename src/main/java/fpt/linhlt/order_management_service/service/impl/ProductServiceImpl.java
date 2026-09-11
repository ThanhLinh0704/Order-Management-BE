package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.CreateProductRequest;
import fpt.linhlt.order_management_service.dto.response.ProductResponse;
import fpt.linhlt.order_management_service.mapper.ProductMapper;
import fpt.linhlt.order_management_service.repository.ProductRepository;
import fpt.linhlt.order_management_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        return null;
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        return List.of();
    }

    @Override
    public ProductResponse getProductById(String productId) {
        return null;
    }
}
