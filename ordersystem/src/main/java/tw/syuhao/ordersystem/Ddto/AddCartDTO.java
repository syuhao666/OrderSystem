package tw.syuhao.ordersystem.Ddto;

import lombok.Data;

@Data
public class AddCartDTO {
    private Long id;  // 商品ID
    private int quantity;    // 商品數量
    // private Long userId;
}
