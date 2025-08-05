package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.DOrder;

public interface DOrderRepository extends JpaRepository<DOrder, Long> {
}
