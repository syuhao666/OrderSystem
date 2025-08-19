package tw.syuhao.ordersystem.Ddto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {
    private int deliveryFee;
    private int floorFee;
    private BigDecimal finalTotal;
    private BigDecimal productTotal;
    private List<CartItemDTO> items; // 購物車明細
    
    private String imageUrl;
}
