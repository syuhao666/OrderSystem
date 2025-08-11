// package tw.syuhao.ordersystem.Dentity;

// import com.fasterxml.jackson.annotation.JsonManagedReference;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;
// import lombok.Data;
// import lombok.ToString;

// @Data
// @Entity
// @Table(name = "users")
// public class User {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)    
//     @Column(name = "id")
//     private Long id;

//     @Column(name = "sub")
//     private String sub;
    
//     @Column(name = "email")
//     private String email;

//     @Column(name = "username")
//     private String name;

//     @Column(name = "password") 
//     private String password;
    
//     @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//     @JsonManagedReference
//     @ToString.Exclude
//     private Cart cart;
    
// }