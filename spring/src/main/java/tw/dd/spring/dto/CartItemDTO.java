package tw.dd.spring.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private String name;
    private int price;
    private int quantity;
    

   
}