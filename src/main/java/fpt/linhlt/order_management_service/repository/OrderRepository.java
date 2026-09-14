package fpt.linhlt.order_management_service.repository;

import fpt.linhlt.order_management_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByCustomer_Id(String customerId);
}
