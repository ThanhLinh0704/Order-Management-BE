package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.CreateProductRequest;
import fpt.linhlt.order_management_service.dto.request.CreateProductVariantRequest;
import fpt.linhlt.order_management_service.dto.response.ProductResponse;
import fpt.linhlt.order_management_service.entity.Product;
import fpt.linhlt.order_management_service.entity.ProductVariant;
import fpt.linhlt.order_management_service.mapper.ProductMapper;
import fpt.linhlt.order_management_service.repository.ProductRepository;
import fpt.linhlt.order_management_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDesciption())
                .build();

        List<ProductVariant> variants = new ArrayList<>();
        for (CreateProductVariantRequest variantRequest : request.getVariants()) {
            ProductVariant productVariant = ProductVariant.builder()
                    .sku(variantRequest.getSku())
                    .size(variantRequest.getSize())
                    .color(variantRequest.getColor())
                    .price(variantRequest.getPrice())
                    .product(product)
                    .build();
            variants.add(productVariant);
        }
        product.setVariants(variants);
        Product newProduct = productRepository.save(product);
        return productMapper.toProductResponse(newProduct);
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return productMapper.toListProductResponse(products);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("khoong cos id"));
        return productMapper.toProductResponse(product);
    }
}
