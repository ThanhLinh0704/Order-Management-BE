package fpt.linhlt.order_management_service.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private String cartId;
    private String userId;
    private List<CartItemResponse> items;
}
