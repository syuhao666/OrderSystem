// package tw.syuhao.ordersystem.Dentity;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;

// import com.fasterxml.jackson.annotation.JsonManagedReference;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.Table;
// import lombok.Data;

// @Data
// @Entity
// @Table(name = "orders")
// public class OrderD {  //特殊+D

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name;
//     private String phone;
//     private String email;
//     private String address;
//     private String paymentMethod;

//     @Column(name = "total_amount")
//     private BigDecimal totalPrice;

    

//     @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//     @JsonManagedReference
//     private List<OrderItem> items = new ArrayList<>();

    

    

// }