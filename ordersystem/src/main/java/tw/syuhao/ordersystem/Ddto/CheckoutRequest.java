package tw.syuhao.ordersystem.Ddto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String deliveryMethod; // "DELIVERY" or "PICKUP"
    private int floor;             // 樓層
}
