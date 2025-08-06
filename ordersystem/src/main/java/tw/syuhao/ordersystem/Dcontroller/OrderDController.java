package tw.syuhao.ordersystem.Dcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import tw.syuhao.ordersystem.Ddto.CartItemDTO;
import tw.syuhao.ordersystem.Ddto.OrderRequestDTO;
import tw.syuhao.ordersystem.Dentity.Cart;
import tw.syuhao.ordersystem.Dentity.OrderD; //特殊+D
import tw.syuhao.ordersystem.Dentity.OrderItem;
import tw.syuhao.ordersystem.Dentity.User;
import tw.syuhao.ordersystem.Drepository.CartItemRepository;
import tw.syuhao.ordersystem.Drepository.CartRepository;
import tw.syuhao.ordersystem.Drepository.OrderDRepository;  //特殊+D
import tw.syuhao.ordersystem.Drepository.ProductDRepository; //特殊+D
import tw.syuhao.ordersystem.Drepository.UsersRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class OrderDController {

    @Autowired
    private OrderDRepository orderRepository; //特殊+D

    @Autowired
    private ProductDRepository productRepository;   //特殊+D

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private CartRepository cartRepository;


    @Transactional
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody OrderRequestDTO dto) {
        // 建立訂單主資料
        
        OrderD order = new OrderD();  //特殊+D
        order.setName(dto.getName());
        order.setPhone(dto.getPhone());
        order.setEmail(dto.getEmail());
        order.setAddress(dto.getAddress());
        order.setPaymentMethod(dto.getPaymentMethod());

       

        // 建立訂單項目資料
        for (CartItemDTO itemDTO : dto.getCart()) {
            OrderItem item = new OrderItem();
            productRepository.findById(itemDTO.getProductId()).ifPresent(item::setProduct);            
            item.setName(itemDTO.getName());
            item.setPrice(itemDTO.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setOrder(order); // 綁定回主訂單
            order.getItems().add(item);
            
        }

        // 儲存訂單
        orderRepository.save(order);

        Long fakeUserId = 2L;

        User user = userRepository.findById(fakeUserId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        Cart cart = cartRepository.findByUser(user)
        .orElseThrow(() -> new RuntimeException("找不到購物車"));   
        cartItemRepository.deleteByCart(cart);

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
