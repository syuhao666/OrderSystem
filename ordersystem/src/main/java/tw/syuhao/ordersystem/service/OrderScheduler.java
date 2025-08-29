package tw.syuhao.ordersystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.syuhao.ordersystem.entity.Order;
import tw.syuhao.ordersystem.entity.OrderItem;
import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.repository.OrderRepository;
import tw.syuhao.ordersystem.repository.ProductRepository;

@Service
public class OrderScheduler {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedRate = 1 * 600 * 1000)
    @Transactional
    public void releaseExpiredOrders() {
        LocalDateTime expireTime = LocalDateTime.now().minusMinutes(30);
        List<Order> expiredOrders = orderRepository.findByStatusAndCreatedAtBefore("PENDING", expireTime);

        for (Order order : expiredOrders) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
            order.setStatus("CANCELLED");   // 改狀態
            orderRepository.save(order);    // 確保寫回資料庫
            
        }
    }
}

