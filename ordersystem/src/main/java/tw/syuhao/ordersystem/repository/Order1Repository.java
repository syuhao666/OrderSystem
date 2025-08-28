package tw.syuhao.ordersystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Order1;

public interface Order1Repository extends JpaRepository<Order1, Integer> {
    // 注意：Users.id 是 Long，所以這裡用 Long
    List<Order1> findByUser_IdOrderByCreatedAtDesc(Long userId);

    Optional<Order1> findByIdAndUser_Id(Integer id, Long userId);
}
