package tw.syuhao.ordersystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.syuhao.ordersystem.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryContaining(String category, Pageable pageable);
    Page<Product> findByNameContainingAndCategoryContaining(String name, String category, Pageable pageable);
}
