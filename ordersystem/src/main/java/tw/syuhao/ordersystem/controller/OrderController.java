package tw.syuhao.ordersystem.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.syuhao.ordersystem.entity.Order;
import tw.syuhao.ordersystem.repository.OrderRepository;
import tw.syuhao.ordersystem.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    // 顯示所有訂單列表
    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin-order";
    }

    // 顯示單一訂單
    @GetMapping("/{id}")
    public String getOrder(@PathVariable Integer id, Model model) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "admin-order-detail"; // 對應詳細資料頁
        } else {
            return "redirect:/admin/orders";
        }
    }

    // 刪除訂單
    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/orders/add")
    public String showAddOrderForm(Model model) {
        return "admin-order-add";
    }

    @PostMapping("/admin/orders/add")
    public String addOrder(@RequestParam("userId") Integer userId,
            @RequestParam("totalAmount") BigDecimal totalAmount,
            @RequestParam("status") String status) {

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);
        return "redirect:/admin/orders";
    }

}
