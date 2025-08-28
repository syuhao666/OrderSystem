package tw.syuhao.ordersystem.Ddto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private String name;

    @JsonProperty("unitPrice")
    private BigDecimal unitPrice;

    private int quantity;
}
