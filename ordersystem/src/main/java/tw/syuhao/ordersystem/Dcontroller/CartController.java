package tw.syuhao.ordersystem.Dcontroller;

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
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // ----------------------------------------------創建購物車
    @PostMapping("/add")
    public ResponseEntity<String> addCart(@RequestBody AddCartDTO cdto, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("請先登入");
        }
        cartService.addCart(user, cdto);
        return ResponseEntity.ok("加入購物車成功");
    }

    // -----------------------------------------------------------
    @PostMapping("/increase")
    public ResponseEntity<String> increaseQuantity(@RequestBody CartItemUpdateDTO dto) {
        cartService.increaseQuantity(dto.getCartItemId());
        return ResponseEntity.ok("數量增加成功");
    }

    @PostMapping("/decrease")
    public ResponseEntity<String> decreaseQuantity(@RequestBody CartItemUpdateDTO dto) {
        cartService.decreaseQuantity(dto.getCartItemId());
        return ResponseEntity.ok("數量減少成功");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItem(@RequestParam Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.ok("商品已移除購物車");
    }

    // -------------------------------------------
    @GetMapping("/count")
    public ResponseEntity<Long> getCartCount(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build(); // 未登入
        }
        long count = cartService.getCartCount(user);
        return ResponseEntity.ok(count);
    }

    // ------------------------------------------- 結帳計算
    @PostMapping("/xa")
    public ResponseEntity<OrderResponse> checkout(@RequestBody CheckoutRequest request, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build(); // 未登入
        }
        OrderResponse response = cartService.checkout(user, request);
        return ResponseEntity.ok(response);
    }
}
