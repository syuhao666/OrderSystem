package tw.syuhao.ordersystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.syuhao.ordersystem.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByNameContaining(String name, Pageable pageable);
    Page<Order> findByPhoneContaining(String phone, Pageable pageable);
    Page<Order> findByStatusContaining(String status, Pageable pageable);
    Page<Order> findByNameContainingAndPhoneContaining(String name, String phone, Pageable pageable);
    Page<Order> findByNameContainingAndStatusContaining(String name, String status, Pageable pageable);
    Page<Order> findByPhoneContainingAndStatusContaining(String phone, String status, Pageable pageable);
    Page<Order> findByNameContainingAndPhoneContainingAndStatusContaining(String name, String phone, String status, Pageable pageable);
    List<Order> findByStatusAndCreatedAtBefore(String status, LocalDateTime time);
}
