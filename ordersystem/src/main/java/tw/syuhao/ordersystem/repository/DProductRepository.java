package tw.syuhao.ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.DProduct;

public interface DProductRepository extends JpaRepository<DProduct, Long> {
    
}

