package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.CreateProductRequest;
import fpt.linhlt.order_management_service.dto.response.ProductResponse;
import fpt.linhlt.order_management_service.entity.Product;
import fpt.linhlt.order_management_service.repository.ProductRepository;
import fpt.linhlt.order_management_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDesciption())
                .build();
        return null;
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        return List.of();
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        return null;
    }
}
