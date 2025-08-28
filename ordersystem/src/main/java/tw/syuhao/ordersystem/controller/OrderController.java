package tw.syuhao.ordersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.syuhao.ordersystem.entity.Order;
import tw.syuhao.ordersystem.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(Model model,
            @RequestParam(defaultValue = "1") int page, // 第幾頁（1 起算）
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String status
    ) {
        int pageIndex = page - 1;
        Page<Order> orderPage = orderService.searchOrders(name, phone, status, pageIndex, size);
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("name", name);
        model.addAttribute("phone", phone);
        model.addAttribute("status", status);
        model.addAttribute("size", size);
        model.addAttribute("activePage", "order");
        return "adminOrder";
    }

    @GetMapping("/new")
    public String newOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order-form";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order) {
        orderService.save(order); // 這裡會自動扣庫存
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
