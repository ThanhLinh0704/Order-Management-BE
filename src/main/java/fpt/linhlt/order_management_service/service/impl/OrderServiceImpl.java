package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.response.OrderResponse;
import fpt.linhlt.order_management_service.entity.Order;
import fpt.linhlt.order_management_service.entity.OrderItem;
import fpt.linhlt.order_management_service.entity.Payment;
import fpt.linhlt.order_management_service.entity.User;
import fpt.linhlt.order_management_service.repository.OrderItemRepository;
import fpt.linhlt.order_management_service.repository.OrderRepository;
import fpt.linhlt.order_management_service.repository.PaymentRepository;
import fpt.linhlt.order_management_service.repository.UserRepository;
import fpt.linhlt.order_management_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập");
        }
        return userRepository.findById(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng"));
    }

    @Override
    public List<OrderResponse> getMyOrders() {
        User user = getCurrentUser();
        List<Order> orders = orderRepository.findAllByCustomer_Id(user.getId());

        List<OrderResponse> responseList = new ArrayList<>();
        for (Order order : orders) {
            // Lấy danh sách item của đơn này và cộng dồn số lượng
            List<OrderItem> items = orderItemRepository.findAllByOrder_Id(order.getId());
            int totalItems = 0;
            for (OrderItem item : items) {
                totalItems += item.getQuantity();
            }

            // Lấy phương thức thanh toán của đơn này
            List<Payment> payments = paymentRepository.findAllByOrder_Id(order.getId());
            String paymentMethod = "N/A";
            if (!payments.isEmpty()) {
                paymentMethod = payments.get(0).getPaymentMethod();
            }

            // Tạo DTO response
            OrderResponse response = OrderResponse.builder()
                    .id(order.getId())
                    .trackingNumber(order.getTrackingNumber())
                    .status(order.getStatus())
                    .createdAt(order.getCreateAt())
                    .paymentMethod(paymentMethod)
                    .totalItems(totalItems)
                    .totalAmount(order.getTotalAmount())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }
}
