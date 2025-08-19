package tw.syuhao.ordersystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import tw.syuhao.ordersystem.entity.Cart;
import tw.syuhao.ordersystem.entity.CartItem;



public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByCart(Cart cart);

    @Transactional
    @Modifying
    @Query("delete from CartItem ci where ci.cart = :cart")
    void deleteByCart(Cart cart);

    @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItem ci WHERE ci.cart = :cart")
    long sumQuantityByCart(@Param("cart") Cart cart);
}
