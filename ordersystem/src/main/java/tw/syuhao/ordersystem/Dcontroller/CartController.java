package tw.syuhao.ordersystem.Dcontroller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.syuhao.ordersystem.Ddto.AddCartDTO;
import tw.syuhao.ordersystem.Ddto.CartItemUpdateDTO;
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
    private ProductRepository productRepository; //特殊+D

    @PostMapping("/add")
    public ResponseEntity<String> addCart(@RequestBody AddCartDTO cdto) {
        // 1. 假設會員固定為 ID = 1
        Long fakeUserId = 2L;
        Users user = userRepository.findById(fakeUserId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        System.err.println(user);

        // 2. 找出或建立使用者的購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 3. 查商品
        Product product = productRepository.findById(cdto.getId())  //特殊+D
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
        cartItemRepository.delete(item);
        return ResponseEntity.ok("商品已移除購物車");
    }

}
