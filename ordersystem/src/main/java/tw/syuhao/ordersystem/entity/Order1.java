package tw.syuhao.ordersystem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "orders")
public class Order1 {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String phone;
    private String email;
    private String address;

    @Column(name = "payment_method")
    private String paymentMethod;

    private String status;

    /** DB: total_amount；前端期望：totalAmount */
    @Column(name = "total_amount")
    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "merchant_trade_no")
    @JsonProperty("merchantTradeNo")
    private String merchantTradeNo;

    @Column(name = "email_sent")
    private boolean emailSent;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem1> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonIgnore
    private Users user;

    // ---- 相容舊程式：別名 getter/setter (totalPrice) ----
    public BigDecimal getTotalPrice() { return this.totalAmount; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalAmount = totalPrice; }
}
