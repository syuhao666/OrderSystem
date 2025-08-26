package tw.syuhao.ordersystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.entity.StockMovement;
import tw.syuhao.ordersystem.repository.ProductRepository;
import tw.syuhao.ordersystem.repository.StockMovementRepository;

@Service
public class StockService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    public int getCurrentStock(Long productId) {
        Integer current = stockMovementRepository.getCurrentStock(productId);
        return current != null ? current : 0;
    }

    public void adjustStock(Long productId, int quantity, String changeType, String note) {
        Product product = productRepository.findById(productId).orElseThrow();

        // 以移動紀錄為真實庫存來源
        if ("OUT".equals(changeType)) {
            int current = getCurrentStock(productId);
            if (current < quantity) {
                throw new IllegalArgumentException("庫存不足！");
            }
        }

        // 紀錄變動
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setChangeType(changeType);
        movement.setQuantity(quantity);
        movement.setNote(note);
        stockMovementRepository.save(movement);
    }

    public List<StockMovement> getMovements(Long productId) {
        return stockMovementRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    public List<StockMovement> getMovementsForAllProducts() {
        return stockMovementRepository.findAll();
    }
}
