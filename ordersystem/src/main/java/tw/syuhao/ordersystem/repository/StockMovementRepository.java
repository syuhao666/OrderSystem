package tw.syuhao.ordersystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.syuhao.ordersystem.entity.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductIdOrderByCreatedAtDesc(Long productId);

    @Query("SELECT COALESCE(SUM(CASE WHEN sm.changeType = 'IN' THEN sm.quantity ELSE -sm.quantity END), 0) FROM StockMovement sm WHERE sm.product.id = :productId")
    Integer getCurrentStock(@Param("productId") Long productId);
}
