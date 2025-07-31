package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.syuhao.ordersystem.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}

