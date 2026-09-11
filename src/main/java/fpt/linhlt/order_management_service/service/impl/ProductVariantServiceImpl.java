package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.response.ProductVariantResponse;
import fpt.linhlt.order_management_service.entity.ProductVariant;
import fpt.linhlt.order_management_service.mapper.ProductMapper;
import fpt.linhlt.order_management_service.repository.ProductVariantRepository;
import fpt.linhlt.order_management_service.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponse getVariantById(String id) {
        ProductVariant variant = productVariantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy biến thể sản phẩm"
                ));
        return productMapper.toProductVariantResponse(variant);
    }
}
