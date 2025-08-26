package tw.syuhao.ordersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.syuhao.ordersystem.entity.Inventory;
import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.repository.InventoryRepository;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory getInventoryByProductId(Integer productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Transactional
    public Inventory updateStock(Integer productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory == null) {
            inventory = new Inventory();
            inventory.setProduct(new Product()); // 簡單綁定商品
            inventory.setQuantity(quantity);
        } else {
            inventory.setQuantity(quantity);
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void increaseStock(Integer productId, Integer amount) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + amount);
            inventoryRepository.save(inventory);
        }
    }

    @Transactional
    public void decreaseStock(Integer productId, Integer amount) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory != null && inventory.getQuantity() >= amount) {
            inventory.setQuantity(inventory.getQuantity() - amount);
            inventoryRepository.save(inventory);
        } else {
            throw new RuntimeException("庫存不足");
        }
    }
}
