package tw.syuhao.ordersystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // phone 建議可為 null（表單可留白）
    @Column(name = "phone", length = 50, nullable = true)
    private String phone;

    // address 建議也允許為 null（表單可留白）
    @Column(name = "address", length = 255, nullable = true)
    private String address;

    // 與 Users 的 mappedBy="users" 對上（名稱必須是 users）
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonBackReference
    private Users users;
}
