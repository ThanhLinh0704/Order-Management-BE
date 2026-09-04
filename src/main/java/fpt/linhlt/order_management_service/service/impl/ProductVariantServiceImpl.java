package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.CreateProductVariantRequest;
import fpt.linhlt.order_management_service.dto.response.ProductVariantResponse;
import fpt.linhlt.order_management_service.repository.ProductVariantRepository;
import fpt.linhlt.order_management_service.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    @Override
    public ProductVariantResponse createProductVariant(CreateProductVariantRequest request) {

        return null;

    }

    @Override
    public List<ProductVariantResponse> getAllProductVariants() {
        return List.of();
    }

    @Override
    public ProductVariantResponse getProductVariantById(Long productVariantId) {
        return null;
    }
}
