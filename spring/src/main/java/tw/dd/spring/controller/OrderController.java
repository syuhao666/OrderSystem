package tw.dd.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.dd.spring.dto.CartItemDTO;
import tw.dd.spring.dto.OrderRequestDTO;
import tw.dd.spring.entity.Order;
import tw.dd.spring.entity.OrderItem;
import tw.dd.spring.repository.OrderRepository;
import tw.dd.spring.repository.ProductRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody OrderRequestDTO dto) {
        // 建立訂單主資料
        
        Order order = new Order();
        order.setName(dto.getName());
        order.setPhone(dto.getPhone());
        order.setEmail(dto.getEmail());

        order.setCity(dto.getCity());
        order.setDistrict(dto.getDistrict());
        order.setZip(dto.getZip());
        order.setAddress(dto.getAddress());

        order.setPaymentMethod(dto.getPaymentMethod());

        // 建立訂單項目資料
        for (CartItemDTO itemDTO : dto.getCart()) {
            
            

            OrderItem item = new OrderItem();

            productRepository.findById(itemDTO.getId()).ifPresent(item::setProduct);
            item.setName(itemDTO.getName());
            item.setPrice(itemDTO.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            
            item.setOrder(order); // 綁定回主訂單
            order.getItems().add(item);
        }

        // 儲存訂單
        orderRepository.save(order);

        return ResponseEntity.ok("訂單成功，訂單 ID: " + order.getId());
    }

    // ------------------------------------------------------------
    // @PostMapping("/checkout")
    // public ResponseEntity<String> checkout(@RequestBody List<CartItemDTO>
    // cartItems) {

    // Order order = new Order();

    // for (CartItemDTO dto : cartItems) {
    // OrderItem item = new OrderItem();
    // item.setName(dto.getP_name());
    // item.setPrice(dto.getP_price());
    // item.setQuantity(dto.getQuantity());
    // item.setOrder(order); // 關聯回訂單
    // order.getItems().add(item);
    // }

    // orderRepository.save(order);

    // return ResponseEntity.ok("訂單已成功建立，ID: " + order.getId());
    // }

    // --------------------------------------------------------
    // @PostMapping("/checkouttest")
    // public ResponseEntity<String> checkout(@RequestBody List<CartItemDTO>
    // cartItems) {

    // // 這裡可以先印出接收到的內容確認
    // cartItems.forEach(item -> {
    // System.out.println(item.getName() + " x " + item.getQuantity());
    // });

    // // 模擬建立訂單，之後再寫入資料庫
    // return ResponseEntity.ok("訂單已建立！");
    // }
}
