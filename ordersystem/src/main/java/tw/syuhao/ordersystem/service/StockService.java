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

    public void adjustStock(Long productId, int quantity, String changeType, String note) {
        Product product = productRepository.findById(productId).orElseThrow();

        // 更新商品庫存
        if (changeType.equals("IN")) {
            product.setStock(product.getStock() + quantity);
        } else if (changeType.equals("OUT")) {
            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("庫存不足！");
            }
            product.setStock(product.getStock() - quantity);
        }
        productRepository.save(product);

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
