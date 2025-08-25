package tw.syuhao.ordersystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.syuhao.ordersystem.entity.Order;
import tw.syuhao.ordersystem.entity.OrderItem;
import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockService stockService;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public void save(Order order) {
        // 若訂單狀態為"已出貨"或"已成立"，則扣庫存
        if ("已出貨".equals(order.getStatus()) || "已付款".equals(order.getStatus())) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                int current = stockService.getCurrentStock(product.getId());
                if (current < item.getQuantity()) {
                    throw new IllegalArgumentException("商品 [" + product.getName() + "] 庫存不足");
                }
                stockService.adjustStock(product.getId(), item.getQuantity(), "OUT", "訂單扣庫存");
            }
        }
        orderRepository.save(order);
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }

    public Page<Order> searchOrders(String name, String phone, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        boolean hasName = name != null && !name.isEmpty();
        boolean hasPhone = phone != null && !phone.isEmpty();
        boolean hasStatus = status != null && !status.isEmpty();

        if (hasName && hasPhone && hasStatus) {
            return orderRepository.findByNameContainingAndPhoneContainingAndStatusContaining(name, phone, status, pageable);
        } else if (hasName && hasPhone) {
            return orderRepository.findByNameContainingAndPhoneContaining(name, phone, pageable);
        } else if (hasName && hasStatus) {
            return orderRepository.findByNameContainingAndStatusContaining(name, status, pageable);
        } else if (hasPhone && hasStatus) {
            return orderRepository.findByPhoneContainingAndStatusContaining(phone, status, pageable);
        } else if (hasName) {
            return orderRepository.findByNameContaining(name, pageable);
        } else if (hasPhone) {
            return orderRepository.findByPhoneContaining(phone, pageable);
        } else if (hasStatus) {
            return orderRepository.findByStatusContaining(status, pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }
}
