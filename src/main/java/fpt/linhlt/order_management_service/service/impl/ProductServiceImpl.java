package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.CreateProductRequest;
import fpt.linhlt.order_management_service.dto.request.CreateProductVariantRequest;
import fpt.linhlt.order_management_service.dto.response.ProductResponse;
import fpt.linhlt.order_management_service.entity.Inventory;
import fpt.linhlt.order_management_service.entity.InventoryItem;
import fpt.linhlt.order_management_service.entity.Product;
import fpt.linhlt.order_management_service.entity.ProductVariant;
import fpt.linhlt.order_management_service.mapper.ProductMapper;
import fpt.linhlt.order_management_service.repository.InventoryItemRepository;
import fpt.linhlt.order_management_service.repository.InventoryRepository;
import fpt.linhlt.order_management_service.repository.ProductRepository;
import fpt.linhlt.order_management_service.repository.ProductVariantRepository;
import fpt.linhlt.order_management_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductMapper productMapper;
    private final InventoryRepository inventoryRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        List<ProductVariant> variants = new ArrayList<>();
        for (CreateProductVariantRequest item : request.getVariants()) {
            ProductVariant variant = ProductVariant.builder()
                    .product(product)
                    .sku(item.getSku())
                    .variantName(item.getVariantName())
                    .attributes(item.getAttributes())
                    .price(item.getPrice())
                    .build();
            variants.add(variant);
        }
        product.setVariants(variants);
        Product savedProduct = productRepository.save(product);

        Inventory defaultInventory = inventoryRepository.findByCode("DEFAULT").orElseGet(() -> {
            Inventory inventory = new Inventory();
            inventory.setCode("DEFAULT");
            inventory.setName("Kho Tổng");
            inventory.setDescription("Kho hàng mặc định");
            inventory.setStatus("ACTIVE");
            return inventoryRepository.save(inventory);
        });

        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (ProductVariant variant : savedProduct.getVariants()) {
            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setInventory(defaultInventory);
            inventoryItem.setProductVariant(variant);
            inventoryItem.setQuantityInStock(10);
            inventoryItems.add(inventoryItem);
        }
        inventoryItemRepository.saveAll(inventoryItems);

        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));
        return productMapper.toProductResponse(product);
    }
}
