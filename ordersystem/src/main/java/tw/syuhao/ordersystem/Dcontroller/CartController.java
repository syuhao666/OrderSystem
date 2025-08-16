package tw.syuhao.ordersystem.Dcontroller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.syuhao.ordersystem.Ddto.AddCartDTO;
import tw.syuhao.ordersystem.Ddto.CartItemUpdateDTO;
import tw.syuhao.ordersystem.Ddto.CheckoutRequest;
import tw.syuhao.ordersystem.Ddto.OrderResponse;
import tw.syuhao.ordersystem.entity.Cart;
import tw.syuhao.ordersystem.entity.CartItem;
import tw.syuhao.ordersystem.entity.Product; //特殊+D
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.CartItemRepository;
import tw.syuhao.ordersystem.repository.CartRepository;
import tw.syuhao.ordersystem.repository.ProductRepository; //特殊+D
import tw.syuhao.ordersystem.repository.UserRepository;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository; // 特殊+D

    // ----------------------------------------------創建購物車
    @PostMapping("/add")
    public ResponseEntity<String> addCart(@RequestBody AddCartDTO cdto, HttpSession session) {
    
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            // 沒登入就回傳錯誤
            return ResponseEntity.status(401).body("請先登入");
        }

        // 2. 找出或建立使用者的購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 3. 查商品
        Product product = productRepository.findById(cdto.getId()) // 特殊+D
                .orElseThrow(() -> new RuntimeException("找不到商品"));

        // 4. 檢查購物車中是否已有該商品
        Optional<CartItem> existingItemOpt = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(cdto.getQuantity());
            newItem.setCart(cart);
            cartItemRepository.save(newItem);
        }

        return ResponseEntity.ok("加入購物車成功");
    }

    // -----------------------------------------------------------
    @PostMapping("/increase")
    public ResponseEntity<String> increaseQuantity(@RequestBody CartItemUpdateDTO dto) {
        CartItem item = cartItemRepository.findById(dto.getCartItemId())
                .orElseThrow(() -> new RuntimeException("找不到購物車商品"));
        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);
        return ResponseEntity.ok("數量增加成功");
    }

    @PostMapping("/decrease")
    public ResponseEntity<String> decreaseQuantity(@RequestBody CartItemUpdateDTO dto) {
        CartItem item = cartItemRepository.findById(dto.getCartItemId())
                .orElseThrow(() -> new RuntimeException("找不到購物車商品"));
        int newQty = item.getQuantity() - 1;
        if (newQty <= 0) {
            // ---新增因為購物車刪不掉
            Cart cart = item.getCart();
            if (cart != null) {
                cart.getCartItems().remove(item);
            }
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQty);
            cartItemRepository.save(item);
        }
        return ResponseEntity.ok("數量減少成功");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItem(@RequestParam Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("找不到購物車商品"));

        // -----新增因為購物車刪不掉
        Cart cart = item.getCart();
        if (cart != null) {
            cart.getCartItems().remove(item); // 從關聯集合移除
        }
        // -----
        cartItemRepository.delete(item);
        return ResponseEntity.ok("商品已移除購物車");
    }

    // -------------------------------------------
    @GetMapping("/count")
    public long getCartCount(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            return 0;
        }
        return cartItemRepository.countByCart(cart);
    }

    // ------------------------------------------- 結帳計算
    @PostMapping("/xa")
    public ResponseEntity<OrderResponse> checkout(@RequestBody CheckoutRequest request, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build(); // 未登入
        }

        // 運送費
        int deliveryFee = 0;
        if ("DELIVERY".equalsIgnoreCase(request.getDeliveryMethod())) {
            deliveryFee = 100; // 假設運送費固定 100 元
        }

        // 樓層費
        int floorFee = 0;
        if (request.getFloor() > 1) {
            floorFee = (request.getFloor() - 1) * 50; // 每層加 50 元
        }

        OrderResponse response = new OrderResponse();
        response.setDeliveryFee(deliveryFee);
        response.setFloorFee(floorFee);

        return ResponseEntity.ok(response);
    }
}
