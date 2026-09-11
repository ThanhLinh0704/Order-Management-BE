package fpt.linhlt.order_management_service.controller;

import fpt.linhlt.order_management_service.dto.response.ProductVariantResponse;
import fpt.linhlt.order_management_service.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-variants")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantResponse> getVariantById(@PathVariable("id") String id) {
        return ResponseEntity.ok(productVariantService.getVariantById(id));
    }
}
