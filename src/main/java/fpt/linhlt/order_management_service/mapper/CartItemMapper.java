package fpt.linhlt.order_management_service.mapper;

import fpt.linhlt.order_management_service.dto.response.CartItemResponse;
import fpt.linhlt.order_management_service.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "cartItemId", source = "id")
    @Mapping(target = "productVariantName", source = "productVariant.variantName")
    @Mapping(target = "sku", source = "productVariant.sku")
    @Mapping(target = "attributes", source = "productVariant.attributes")
    @Mapping(target = "price", source = "productVariant.price")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "inventoryStatus", ignore = true)
    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toListCartItemResponse(List<CartItem> cartItems);
}
