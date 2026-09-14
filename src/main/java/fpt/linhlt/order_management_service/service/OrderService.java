package fpt.linhlt.order_management_service.service;

import fpt.linhlt.order_management_service.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getMyOrders();
}

