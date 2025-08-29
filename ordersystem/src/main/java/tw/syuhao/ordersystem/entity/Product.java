package tw.syuhao.ordersystem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "product")
@SQLRestriction("deleted = false")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price; // Integer 改 BigDecimal 08091926 家綸

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "image_url")
    private String imageUrl;

    private String description;

    private Boolean enabled;

    @Column(nullable = false)
    private String status;

    private String category;

    @Column(nullable = false)
    private boolean deleted = false;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    @ToString.Exclude
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private ProductSpecification specification;

    public void setSpecification(ProductSpecification specification) {
        this.specification = specification;
        specification.setProduct(this); // 綁定雙向關聯
    }

    public ProductSpecification getSpecification() {
        return specification;
    }

}
