package tw.syuhao.ordersystem.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public void save(Product product) {
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        repo.save(product);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Product findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // public Page<Product> searchProducts(String name, String status,
    // BigDecimal minPrice, BigDecimal maxPrice,
    // int page, int size) {
    // Specification<Product> spec = (root, query, cb) -> {
    // List<Predicate> predicates = new ArrayList<>();

    // if (name != null && !name.isEmpty()) {
    // predicates.add(cb.like(root.get("name"), "%" + name + "%"));
    // }
    // if (status != null && !status.isEmpty()) {
    // predicates.add(cb.equal(root.get("status"), status));
    // }
    // if (minPrice != null) {
    // predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
    // }
    // if (maxPrice != null) {
    // predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
    // }

    // return cb.and(predicates.toArray(new Predicate[0]));
    // };

    // Pageable pageable = PageRequest.of(page, size,
    // Sort.by("createdAt").descending());

    // return repo.findAll(spec, pageable);
    // }

    public Page<Product> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        if (keyword != null && !keyword.trim().isEmpty()) {
            return repo.findByNameContaining(keyword, pageable);
        } else {
            return repo.findAll(pageable);
        }
    }

    public Page<Product> findProducts(String name, String category, Integer page, Integer size,
                                      BigDecimal minPrice, BigDecimal maxPrice) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        boolean hasName = name != null && !name.isEmpty();
        boolean hasCategory = category != null && !category.isEmpty();
        boolean hasMin = minPrice != null;
        boolean hasMax = maxPrice != null;

        if (hasName && hasCategory && hasMin && hasMax) {
            return repo.findByNameContainingAndCategoryContainingAndPriceBetween(name, category, minPrice, maxPrice, pageable);
        } else if (hasName && hasMin && hasMax) {
            return repo.findByNameContainingAndPriceBetween(name, minPrice, maxPrice, pageable);
        } else if (hasCategory && hasMin && hasMax) {
            return repo.findByCategoryContainingAndPriceBetween(category, minPrice, maxPrice, pageable);
        } else if (hasMin && hasMax) {
            return repo.findByPriceBetween(minPrice, maxPrice, pageable);
        } else if (hasName && hasCategory) {
            return repo.findByNameContainingAndCategoryContaining(name, category, pageable);
        } else if (hasName) {
            return repo.findByNameContaining(name, pageable);
        } else if (hasCategory) {
            return repo.findByCategoryContaining(category, pageable);
        } else {
            return repo.findAll(pageable);
        }
    }

    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repo.findAll(pageable);
    }
}
