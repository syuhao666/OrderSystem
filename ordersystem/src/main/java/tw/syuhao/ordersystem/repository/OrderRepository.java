package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.syuhao.ordersystem.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
