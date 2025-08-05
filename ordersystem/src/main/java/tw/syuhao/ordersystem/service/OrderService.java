package tw.syuhao.ordersystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.syuhao.ordersystem.entity.Order;
import tw.syuhao.ordersystem.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }
}

