package tw.syuhao.ordersystem.Dcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.syuhao.ordersystem.Ddto.CartItemResponseDTO;
import tw.syuhao.ordersystem.Dentity.Cart;
import tw.syuhao.ordersystem.Dentity.CartItem;
import tw.syuhao.ordersystem.Dentity.ProductD;  //特殊+D
import tw.syuhao.ordersystem.Dentity.User;
import tw.syuhao.ordersystem.Drepository.CartItemRepository;
import tw.syuhao.ordersystem.Drepository.CartRepository;
import tw.syuhao.ordersystem.Drepository.ProductDRepository; //特殊+D
import tw.syuhao.ordersystem.Drepository.UsersRepository;




@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductDRepository productRepository; //特殊+D

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UsersRepository userRepository;

    @GetMapping("/products")
    public List<ProductD> getAllProducts() {  //特殊+D
        return productRepository.findAll();
    }

    @GetMapping("/carts")
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItems() {
        Long fakeUserId = 2L;

        User user = userRepository.findById(fakeUserId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("使用者尚未建立購物車"));

        List<CartItem> items = cartItemRepository.findByCart(cart);

        List<CartItemResponseDTO> responseList = items.stream()
                .map(CartItemResponseDTO::new)
                .toList();

        return ResponseEntity.ok(responseList);
    }

}
