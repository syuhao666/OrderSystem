package tw.syuhao.ordersystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tw.syuhao.ordersystem.Ddto.OrderDetailDTO;
import tw.syuhao.ordersystem.Ddto.OrderItemDTO;
import tw.syuhao.ordersystem.Ddto.OrderSummaryDTO;
import tw.syuhao.ordersystem.entity.Order1;
import tw.syuhao.ordersystem.entity.OrderItem1;
import tw.syuhao.ordersystem.entity.Users;
import tw.syuhao.ordersystem.repository.Order1Repository;

@Service
@RequiredArgsConstructor
public class Order1Service {

    private final Order1Repository orderRepository;

    /** 取得該使用者的訂單（新到舊） */
    public List<OrderSummaryDTO> listOrdersOf(Users user) {
        return orderRepository.findByUser_IdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    /** 取得該使用者的某筆訂單詳情（若非本人則回傳 null） */
    public OrderDetailDTO getOrderDetailOfUser(Integer orderId, Users user) {
        return orderRepository.findByIdAndUser_Id(orderId, user.getId())
                .map(this::toDetailDTO)
                .orElse(null);
    }

    /* ------------ Entity -> DTO 轉換 ------------- */

    private OrderSummaryDTO toSummaryDTO(Order1 o) {
        return new OrderSummaryDTO(
                o.getId(),
                o.getStatus(),
                o.getTotalAmount(),      // BigDecimal totalAmount
                o.getCreatedAt(),        // LocalDateTime
                o.getPaymentMethod(),
                o.getMerchantTradeNo()
        );
    }

    private OrderDetailDTO toDetailDTO(Order1 o) {
        return new OrderDetailDTO(
                o.getId(),
                o.getStatus(),
                o.getTotalAmount(),
                o.getCreatedAt(),
                o.getPaymentMethod(),
                o.getMerchantTradeNo(),
                o.getName(),
                o.getPhone(),
                o.getEmail(),
                o.getAddress(),
                o.getItems().stream().map(this::toItemDTO).collect(Collectors.toList())
        );
    }

    private OrderItemDTO toItemDTO(OrderItem1 i) {
        return new OrderItemDTO(
                i.getId(),
                i.getName(),
                i.getUnitPrice(),   // 注意：對應欄位 unitPrice
                i.getQuantity()
        );
    }
}
