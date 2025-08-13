package tw.syuhao.ordersystem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;  // Integer  改  BigDecimal 08091926  家綸

    private Integer stock;

    @Column(name = "image_url")
    private String imageUrl;

    private String description;

    private Boolean enabled;

    private String status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    @ToString.Exclude
    private List<CartItem> cartItems = new ArrayList<>();
}
