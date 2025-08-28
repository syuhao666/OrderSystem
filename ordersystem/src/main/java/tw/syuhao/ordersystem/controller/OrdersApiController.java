package tw.syuhao.ordersystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.service.Order1Service;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersApiController {

     private final Order1Service order1Service;

    @GetMapping
    public ResponseEntity<?> listOrders(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(order1Service.listOrdersOf(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> orderDetail(@PathVariable Integer id, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();
        var dto = order1Service.getOrderDetailOfUser(id, user);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }
}