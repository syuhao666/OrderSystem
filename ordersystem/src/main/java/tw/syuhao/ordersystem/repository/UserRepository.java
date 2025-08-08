package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long>{
    Users findByUsername(String username);
}
