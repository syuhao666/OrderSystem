package tw.syuhao.ordersystem.Ddto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private List<CartItemDTO> cart;     //購物車內容物
    private String name;    //收件人名字
    private String phone;   //收件人電話
    private String email;   //收件人電子郵件
    private String address;     //收件人地址
    private String paymentMethod;   //付款方式
    private BigDecimal totalPrice;   //付款方式
    
    
    
}