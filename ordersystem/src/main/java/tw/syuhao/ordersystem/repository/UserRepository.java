package tw.syuhao.ordersystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.syuhao.ordersystem.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long>{
     // 列表頁專用：一次撈 users 並帶出 address，避免 N+1 與被其他關聯牽走
    @Query("""
      select u from Users u
      left join fetch u.address
    """)
    List<Users> findAllWithAddress();
    
    Users findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
  
    Users findByUsername(String username);
    
    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.address a WHERE " +
           "(:keyword IS NULL OR u.username LIKE %:keyword% OR u.email LIKE %:keyword% OR a.address LIKE %:keyword%)")
    Page<Users> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
