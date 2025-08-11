package tw.syuhao.ordersystem.Dcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import tw.syuhao.ordersystem.Ddto.CartItemDTO;
import tw.syuhao.ordersystem.Ddto.OrderRequestDTO;
import tw.syuhao.ordersystem.entity.Cart;
import tw.syuhao.ordersystem.entity.Order;
import tw.syuhao.ordersystem.entity.OrderItem;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.CartItemRepository;
import tw.syuhao.ordersystem.repository.CartRepository;
import tw.syuhao.ordersystem.repository.OrderRepository;
import tw.syuhao.ordersystem.repository.ProductRepository;
import tw.syuhao.ordersystem.repository.UserRepository; //特殊+D

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class OrderDController {

    @Autowired
    private OrderRepository orderRepository; // 特殊+D

    @Autowired
    private ProductRepository productRepository; // 特殊+D

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Transactional
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody OrderRequestDTO dto, HttpSession session) {

        // 1. 從 Session 拿使用者
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("請先登入");
        }

        // 1. 建立訂單主資料
        Order order = new Order();
        order.setName(dto.getName());
        order.setPhone(dto.getPhone());
        order.setEmail(dto.getEmail());
        order.setAddress(dto.getAddress());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setTotalPrice(dto.getTotalPrice());
        // order.setUser(user);
        userRepository.findById(user.getId()).ifPresent(order::setUser);
        System.out.println(order);
        // 2. 建立訂單項目資料
        for (CartItemDTO itemDTO : dto.getCart()) {
            OrderItem item = new OrderItem();
            productRepository.findById(itemDTO.getProductId()).ifPresent(item::setProduct);
            item.setName(itemDTO.getName());
            item.setPrice(itemDTO.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setOrder(order);
            order.getItems().add(item);
        }

        // 3. 儲存訂單
        orderRepository.save(order);

        // 5. 清空購物車，要用真正登入使用者的購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("找不到購物車"));
        cartItemRepository.deleteByCart(cart);

        // 5. 呼叫 n8n 付款流程 webhook（改用 exchange 搭配 HttpEntity）
        RestTemplate restTemplate = new RestTemplate();
        String n8nUrl = "https://accurately-enhanced-raven.ngrok-free.app/webhook/ecpay/dd";

        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("orderId", order.getId());
        paymentData.put("name", order.getName());
        paymentData.put("totalAmount", order.getTotalPrice());
        paymentData.put("items", dto.getCart());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(paymentData, headers);

        String ecpayHtml = "";
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    n8nUrl,
                    org.springframework.http.HttpMethod.POST,
                    requestEntity,
                    String.class);

            // System.out.println("HTTP Status: " + response.getStatusCode());
            // System.out.println("Response body: " + response.getBody());
            ecpayHtml = response.getBody();
            // System.out.println("n8n 回傳 HTML:\n" + ecpayHtml);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("呼叫付款流程失敗: " + e.getMessage());
        }

        // 6. 回傳 HTML 給前端
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(ecpayHtml);

    }

}

