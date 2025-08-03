package tw.dd.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.dd.spring.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}

