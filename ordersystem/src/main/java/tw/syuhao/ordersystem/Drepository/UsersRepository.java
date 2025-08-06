package tw.syuhao.ordersystem.Drepository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.Dentity.User;





public interface UsersRepository extends JpaRepository<User, Long> {
    // JpaRepository 提供了基本的 CRUD 操作
    // 可以在這裡添加自定義查詢方法
    User findBySub(String sub);

}
