package com.example.shopping.repository;

import com.example.shopping.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * 管理者商品Repositoryインターフェース
 */
public interface AdminProductRepository {
    Page<Product> findByNameContainingIgnoreCaseOrderByIdDesc(String name, Pageable pageable);

    Page<Product> findByCategoryOrderByIdDesc(String category, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc(String name, String category,
            Pageable pageable);

    List<Product> findByStockQuantityLessThanEqualOrderByIdDesc(Integer stockQuantity);

    Page<Product> findAllByOrderByIdDesc(Pageable pageable);

    // 追加
    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Product> findAll();
}