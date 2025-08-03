package tw.dd.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.dd.spring.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
