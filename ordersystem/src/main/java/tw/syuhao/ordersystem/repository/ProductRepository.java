package tw.syuhao.ordersystem.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tw.syuhao.ordersystem.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryContaining(String category, Pageable pageable);
    Page<Product> findByNameContainingAndCategoryContaining(String name, String category, Pageable pageable);
    Page<Product> findByNameContainingAndCategoryContainingAndPriceBetween(
        String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> findByNameContainingAndPriceBetween(
        String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> findByCategoryContainingAndPriceBetween(
        String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> findByPriceBetween(
        BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
