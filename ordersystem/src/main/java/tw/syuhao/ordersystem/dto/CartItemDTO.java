package tw.syuhao.ordersystem.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;    // 商品ID
    private String name;    // 商品名稱
    private BigDecimal price;   // 商品價格
    private int quantity;   // 商品數量
    

   
}