package tw.syuhao.ordersystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.syuhao.ordersystem.entity.Order;
import tw.syuhao.ordersystem.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // 取得所有訂單
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 根據 ID 查詢訂單
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    // 新增或更新訂單
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // 刪除訂單
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}
