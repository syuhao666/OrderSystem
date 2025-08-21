package tw.syuhao.ordersystem.Ddto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDetailDTO {
    private Integer id;
    private String status;

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private String paymentMethod;

    @JsonProperty("merchantTradeNo")
    private String merchantTradeNo;

    // 收件/聯絡資訊
    private String name;
    private String phone;
    private String email;
    private String address;

    private List<OrderItemDTO> items;
}
