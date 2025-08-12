package tw.syuhao.ordersystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Users;

public interface CustomerRepository extends JpaRepository<Users, Long> {
}
