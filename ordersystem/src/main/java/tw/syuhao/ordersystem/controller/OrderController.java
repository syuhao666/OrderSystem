package tw.syuhao.ordersystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tw.syuhao.ordersystem.entity.Order;
import tw.syuhao.ordersystem.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "adminOrder";
    }

    @GetMapping("/new")
    public String newOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order-form";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order) {
        orderService.save(order);
        return "redirect:/admin/orders";
    }

    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Integer id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.delete(id);
        return "redirect:/admin/orders";
    }
}

