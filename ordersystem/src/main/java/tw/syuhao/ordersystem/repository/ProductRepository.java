package tw.syuhao.ordersystem.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import tw.syuhao.ordersystem.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
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

        Page<Product> findByNameContainingAndCategoryContainingAndPriceBetweenAndStatus(
                        String name, String category, BigDecimal minPrice, BigDecimal maxPrice,
                        String status, Pageable pageable);

        @Override
        @EntityGraph(attributePaths = { "specification" })
        List<Product> findAll();

        // ✅ 特殊方法：查詢所有商品（包含 deleted = true 的）
        @Query("SELECT p FROM Product p")
        List<Product> findAllIncludingDeleted();

        @Query("SELECT p FROM Product p WHERE p.deleted = true")
        Page<Product> findByDeletedTrue(Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.id = :id")
        Optional<Product> findByIdIncludingDeleted(@Param("id") Long id);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT p FROM Product p WHERE p.id = :id")
        Optional<Product> findByIdForUpdate(@Param("id") Long id);
}
