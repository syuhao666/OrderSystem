package tw.syuhao.ordersystem.Ddto;

import java.math.BigDecimal;

import lombok.Data;
import tw.syuhao.ordersystem.entity.CartItem;



@Data
public class CartItemResponseDTO {
    private Long cartItemId; // ✅ CartItem 的 ID
    private Long productId;  // 商品 ID
    private String name;     // 商品名稱
    private BigDecimal price;// 商品價格
    private int quantity;    // 數量
    private String imageUrl;

    public CartItemResponseDTO(CartItem item) {
        this.cartItemId = item.getId(); // ✅ 這才是要傳給後端的 ID
        this.productId = item.getProduct().getId();
        this.name = item.getProduct().getName();
        this.price = item.getProduct().getPrice();
        this.quantity = item.getQuantity();
        this.imageUrl = item.getProduct().getImageUrl();
    }
}