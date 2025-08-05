package tw.syuhao.ordersystem.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_item")
public class DOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "unit_price")
    private BigDecimal price;

    private int quantity;


    //--------------------------------
    @ManyToOne
    @JoinColumn(name = "order_id")
    private DOrder order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private DProduct product;

}