package com.example.shopping.repository;

import com.example.shopping.model.entity.Order;
import java.util.Optional;
import java.util.List;

/**
 * ユーザー注文Repositoryインターフェース
 */
public interface UserOrderRepository {
    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // 追加
    Optional<Order> findById(Long id);

    Order save(Order order);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Order> findAll();
}