package com.example.shopping.repository;

import com.example.shopping.model.entity.Product;
import java.util.List;
import java.util.Optional;

/**
 * ユーザーカートRepositoryインターフェース
 */
public interface UserCartRepository {
    Optional<Product> findByIdAndStockQuantityGreaterThan(Long productId, Integer stockQuantity);

    List<Product> findByIdInAndStockQuantityGreaterThanZero(List<Long> productIds);

    // 追加
    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Product> findAll();
}