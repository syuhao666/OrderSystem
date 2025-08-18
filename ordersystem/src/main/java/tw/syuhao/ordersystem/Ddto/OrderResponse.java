package tw.syuhao.ordersystem.Ddto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderResponse {
    private int deliveryFee;
    private int floorFee;
    private BigDecimal finalTotal;
    private BigDecimal productTotal;
}
