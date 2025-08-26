package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findByProductId(Integer productId);
}
