package tw.syuhao.ordersystem.service;

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

    public Page<Product> findProducts(String name, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        if (name != null && !name.isEmpty() && category != null && !category.isEmpty()) {
            return repo.findByNameContainingAndCategoryContaining(name, category, pageable);
        } else if (name != null && !name.isEmpty()) {
            return repo.findByNameContaining(name, pageable);
        } else if (category != null && !category.isEmpty()) {
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
