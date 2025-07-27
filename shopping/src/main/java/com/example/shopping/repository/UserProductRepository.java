package com.example.shopping.repository;

import com.example.shopping.model.entity.Product;
import java.util.List;
import java.util.Optional;

/**
 * ユーザー商品Repositoryインターフェース
 */
public interface UserProductRepository {
    List<Product> findAllByOrderByIdAsc();

    List<Product> findByCategoryOrderByIdAsc(String category);

    List<Product> findAvailableProducts();

    List<Product> findAvailableProductsByCategory(String category);

    // 追加
    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Product> findAll();
}