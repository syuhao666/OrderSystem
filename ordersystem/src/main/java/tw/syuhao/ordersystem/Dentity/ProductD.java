// package tw.syuhao.ordersystem.Dentity;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;

// import com.fasterxml.jackson.annotation.JsonBackReference;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.Table;
// import lombok.Data;
// import lombok.ToString;

// @Data
// @Entity
// @Table(name = "product")
// public class ProductD {  //特殊+D

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(name = "name")
//     private String name;

//     @Column(name = "price")
//     private BigDecimal price;

//     @Column(name = "image_url")
//     private String imageUrl;

//     private String description;

//     @OneToMany(mappedBy = "product")
//     @JsonBackReference
//     @ToString.Exclude
//     private List<CartItem> cartItems = new ArrayList<>();
// }
