package tw.syuhao.ordersystem.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import tw.syuhao.ordersystem.entity.Product;
import tw.syuhao.ordersystem.repository.ProductRepository;
import tw.syuhao.ordersystem.repository.StockMovementRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    private int computeCurrentStock(Long productId) {
        Integer current = stockMovementRepository.getCurrentStock(productId);
        return current != null ? current : 0;
    }

    public List<Product> getAllProducts() {
        List<Product> products = repo.findAll();
        for (Product product : products) {
            if (product.getId() != null) {
                product.setStock(computeCurrentStock(product.getId()));
            }
        }
        return products;
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

    // public Product findById(Long id) {
    // return repo.findById(id).orElse(null);
    // }

    public Product findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到商品 id=" + id));
    }

    public Page<Product> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        if (keyword != null && !keyword.trim().isEmpty()) {
            return repo.findByNameContaining(keyword, pageable);
        } else {
            return repo.findAll(pageable);
        }
    }

    // public Page<Product> findProducts(String name, String category, Integer page,
    // Integer size,
    // BigDecimal minPrice, BigDecimal maxPrice, String status) {
    // Pageable pageable = PageRequest.of(page - 1, size,
    // Sort.by("id").descending());

    // boolean hasName = name != null && !name.isEmpty();
    // boolean hasCategory = category != null && !category.isEmpty();
    // boolean hasMin = minPrice != null;
    // boolean hasMax = maxPrice != null;

    // if (hasName && hasCategory && hasMin && hasMax) {
    // return repo.findByNameContainingAndCategoryContainingAndPriceBetween(name,
    // category, minPrice, maxPrice, pageable);
    // } else if (hasName && hasMin && hasMax) {
    // return repo.findByNameContainingAndPriceBetween(name, minPrice, maxPrice,
    // pageable);
    // } else if (hasCategory && hasMin && hasMax) {
    // return repo.findByCategoryContainingAndPriceBetween(category, minPrice,
    // maxPrice, pageable);
    // } else if (hasMin && hasMax) {
    // return repo.findByPriceBetween(minPrice, maxPrice, pageable);
    // } else if (hasName && hasCategory) {
    // return repo.findByNameContainingAndCategoryContaining(name, category,
    // pageable);
    // } else if (hasName) {
    // return repo.findByNameContaining(name, pageable);
    // } else if (hasCategory) {
    // return repo.findByCategoryContaining(category, pageable);
    // } else {
    // return repo.findAll(pageable);
    // }
    // }

    public Page<Product> findProducts(String name, String category, Integer page, Integer size,
            BigDecimal minPrice, BigDecimal maxPrice, String status) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.like(root.get("category"), "%" + category + "%"));
            }
            if (minPrice != null && maxPrice != null) {
                predicates.add(cb.between(root.get("price"), minPrice, maxPrice));
            } else if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            } else if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> pageResult = repo.findAll(spec, pageable);
        for (Product product : pageResult.getContent()) {
            if (product.getId() != null) {
                product.setStock(computeCurrentStock(product.getId()));
            }
        }
        return pageResult;
    }

    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repo.findAll(pageable);
    }

    public void softDeleteProduct(Long productId) {
        Product product = repo.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        product.setDeleted(true);
        repo.save(product);
    }

    public List<Product> getAllActiveProducts() {
        return repo.findAllIncludingDeleted();
    }

    public void softDelete(Long id) {
        Product product = repo.findByIdIncludingDeleted(id).orElse(null);
        if (product != null) {
            product.setDeleted(true);
            repo.save(product);
        }
    }

    public void restore(Long id) {
        Product product = repo.findByIdIncludingDeleted(id).orElse(null);
        if (product != null) {
            product.setDeleted(false);
            repo.save(product);
        }
    }

    public void hardDelete(Long id) {
        repo.deleteById(id);
    }

    public Page<Product> findDeletedProducts(Pageable pageable) {
        return repo.findByDeletedTrue(pageable);
    }

    public void adjustStock(Long id, String changeType, int quantity) {
        Optional<Product> product = repo.findById(id);
        Product p = product.get();
        int stock = p.getStock();
        if(changeType.equals("IN")) {
            stock = stock + quantity;
        }else if (changeType.equals("OUT")){
            stock = stock - quantity;
        }
        p.setStock(stock);
        repo.save(p);
    }
}
