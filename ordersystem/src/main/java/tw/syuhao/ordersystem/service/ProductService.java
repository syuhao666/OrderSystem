package tw.syuhao.ordersystem.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
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

    public Page<Product> searchProducts(String name, String status,
            BigDecimal minPrice, BigDecimal maxPrice,
            int page, int size) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return repo.findAll(spec, pageable);
    }

    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repo.findAll(pageable);
    }
}
