package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.ProductSpecification;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    
}
