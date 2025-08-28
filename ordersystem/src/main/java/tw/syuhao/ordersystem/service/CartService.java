package tw.syuhao.ordersystem.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.syuhao.ordersystem.Ddto.AddCartDTO;
import tw.syuhao.ordersystem.Ddto.CartItemDTO;
import tw.syuhao.ordersystem.Ddto.CheckoutRequest;
import tw.syuhao.ordersystem.Ddto.OrderResponse;
import tw.syuhao.ordersystem.entity.Cart;
import tw.syuhao.ordersystem.entity.CartItem;
import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.CartItemRepository;
import tw.syuhao.ordersystem.repository.CartRepository;
import tw.syuhao.ordersystem.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // 建立購物車
    public void addCart(Users user, AddCartDTO cdto) {

        // 找出或建立使用者的購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 查商品
        Product product = productRepository.findById(cdto.getId())
                .orElseThrow(() -> new RuntimeException("找不到商品"));

        // 檢查購物車中是否已有該商品
        Optional<CartItem> existingItemOpt = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + cdto.getQuantity());
            int newQty = existingItem.getQuantity();
            BigDecimal total = existingItem.getProduct().getPrice().multiply(BigDecimal.valueOf(newQty));
            existingItem.setTotalPrice(total);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(cdto.getQuantity());
            newItem.setTotalPrice(product.getPrice());
            newItem.setCart(cart);
            cartItemRepository.save(newItem);
        }
    }

    // 數量增加
    public void increaseQuantity(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("找不到購物車商品"));
        int newQty = item.getQuantity() + 1;
        BigDecimal total = item.getProduct().getPrice().multiply(BigDecimal.valueOf(newQty));
        item.setQuantity(newQty);
        item.setTotalPrice(total);
        cartItemRepository.save(item);
    }

    // 數量減少 (數量為 0 時刪除)
    public void decreaseQuantity(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("找不到購物車商品"));

        int newQty = item.getQuantity() - 1;
        BigDecimal total = item.getProduct().getPrice().multiply(BigDecimal.valueOf(newQty));
        if (newQty <= 0) {
            Cart cart = item.getCart();
            if (cart != null) {
                cart.getCartItems().remove(item); // 從關聯中移除
            }
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQty);
            item.setTotalPrice(total);
            cartItemRepository.save(item);
        }
    }

    // 移除商品
    public void removeItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("找不到購物車商品"));
        Cart cart = item.getCart();
        if (cart != null) {
            cart.getCartItems().remove(item); // 從關聯集合移除
        }
        cartItemRepository.delete(item);
    }

    // -購物車內容數量
    public long getCartCount(Users user) {
        Optional<Cart> cartOpt = cartRepository.findByUser(user);
        if (cartOpt.isEmpty()) {
            return 0L; // 沒有購物車
        }
        return cartItemRepository.sumQuantityByCart(cartOpt.get());
    }

    public OrderResponse checkout(Users user, CheckoutRequest request) {
        // 取得使用者購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));

        BigDecimal productTotal = BigDecimal.ZERO;
        List<CartItemDTO> items = new ArrayList<>();
        

        for (CartItem item : cart.getCartItems()) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            productTotal = productTotal.add(itemTotal);

            CartItemDTO dto = new CartItemDTO();
            dto.setProductId(item.getProduct().getId());
            dto.setName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setQuantity(item.getQuantity());
            items.add(dto);
            dto.setImageUrl(item.getProduct().getImageUrl());
        }

        // 運送費
        int deliveryFee = "DELIVERY".equalsIgnoreCase(request.getDeliveryMethod()) ? 100 : 0;

        // 樓層費
        int floorFee = request.getFloor() > 1 ? (request.getFloor() - 1) * 50 : 0;

        // 總金額
        BigDecimal finalTotal = productTotal
                .add(BigDecimal.valueOf(deliveryFee))
                .add(BigDecimal.valueOf(floorFee));

        OrderResponse response = new OrderResponse();
        response.setProductTotal(productTotal);
        response.setDeliveryFee(deliveryFee);
        response.setFloorFee(floorFee);
        response.setFinalTotal(finalTotal);
        response.setItems(items); // ✅ 把購物車明細放進 response

        return response;
    }
}
