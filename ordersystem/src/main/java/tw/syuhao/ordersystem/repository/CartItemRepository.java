package tw.syuhao.ordersystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Cart;
import tw.syuhao.ordersystem.entity.CartItem;



public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByCart(Cart cart);
    void deleteByCart(Cart cart);
}
