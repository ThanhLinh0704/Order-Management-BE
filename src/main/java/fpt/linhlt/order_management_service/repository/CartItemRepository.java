package fpt.linhlt.order_management_service.repository;

import fpt.linhlt.order_management_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCart_IdAndProductVariant_Id(String cartId, String productVariantId);

    List<CartItem> findAllByCart_Id(String cartId);
}
