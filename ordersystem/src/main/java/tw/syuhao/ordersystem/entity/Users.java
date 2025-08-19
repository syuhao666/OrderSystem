package tw.syuhao.ordersystem.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role;
    private String remark; //客戶備註

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private Cart cart;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Order> order;
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;
    
     // 一對一：User <-> Address
    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @JsonManagedReference
    // @ToString.Exclude
    // private Cart cart;

    @PrePersist
    void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }

     // 工具方法（可選）：維護雙向關聯一致性
    public void attachAddress(Address addr) {
        this.address = addr;
        if (addr != null) addr.setUsers(this);
    }
}
