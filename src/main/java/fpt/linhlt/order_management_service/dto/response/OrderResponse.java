package fpt.linhlt.order_management_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private String id;
    private String trackingNumber;
    private String status;
    private LocalDateTime createdAt;
    private String paymentMethod;
    private Integer totalItems;
    private BigDecimal totalAmount;
}

