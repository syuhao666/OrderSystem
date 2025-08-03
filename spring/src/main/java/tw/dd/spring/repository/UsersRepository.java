package tw.dd.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.dd.spring.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    // JpaRepository 提供了基本的 CRUD 操作
    // 可以在這裡添加自定義查詢方法
    Users findBySub(String sub);

}
