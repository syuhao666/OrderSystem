package tw.syuhao.ordersystem.Ddto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long productId;  // 商品 ID
    private String name;     // 商品名稱
    private BigDecimal price;// 商品價格
    private int quantity;    // 數量
    private String imageUrl;  
}