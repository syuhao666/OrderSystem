package tw.dd.spring.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private String p_name;
    private int p_price;
    private int quantity;

   
}