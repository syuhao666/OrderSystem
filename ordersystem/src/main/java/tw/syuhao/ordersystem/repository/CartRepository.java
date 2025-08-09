package tw.syuhao.ordersystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Cart;
import tw.syuhao.ordersystem.entity.Users;





public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByUser(Users user);
    
}
