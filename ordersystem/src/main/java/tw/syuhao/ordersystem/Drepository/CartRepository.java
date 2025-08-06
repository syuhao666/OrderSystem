package tw.syuhao.ordersystem.Drepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.Dentity.Cart;
import tw.syuhao.ordersystem.Dentity.User;





public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByUser(User user);
    
}
