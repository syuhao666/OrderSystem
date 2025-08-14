package tw.syuhao.ordersystem.Dcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.syuhao.ordersystem.Ddto.CartItemResponseDTO;
import tw.syuhao.ordersystem.entity.Cart;
import tw.syuhao.ordersystem.entity.CartItem;
import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.CartItemRepository; //特殊+D
import tw.syuhao.ordersystem.repository.CartRepository;
import tw.syuhao.ordersystem.repository.ProductRepository;
import tw.syuhao.ordersystem.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository; // 特殊+D

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/products")
    public List<Product> getAllProducts() { // 特殊+D
        return productRepository.findAll();
    }

    @GetMapping("/carts")
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItems(HttpSession session) {

        // 從 Session 取得目前登入的使用者
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            // 沒登入，回 401 Unauthorized 或你想要的錯誤
            return ResponseEntity.status(401).build();
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("使用者尚未建立購物車"));

        List<CartItem> items = cartItemRepository.findByCart(cart);

        List<CartItemResponseDTO> responseList = items.stream()
                .map(CartItemResponseDTO::new)
                .toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/category")
    public Page<Product> getProductsByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryContaining(category, pageable);
    }

}
