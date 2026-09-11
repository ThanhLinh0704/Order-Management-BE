package fpt.linhlt.order_management_service.repository;

import fpt.linhlt.order_management_service.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {

    Optional<InventoryItem> findByProductVariant_Id(String variantId);
}
