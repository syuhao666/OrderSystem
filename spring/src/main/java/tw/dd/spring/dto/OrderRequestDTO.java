package tw.dd.spring.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private List<CartItemDTO> cart;
    private String name;
    private String phone;
    private String email;
    
    private String city;
    private String district;
    private String zip;
    private String address;
    
    private String paymentMethod;
    
    
    
}