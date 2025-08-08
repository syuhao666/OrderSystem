package tw.syuhao.ordersystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
