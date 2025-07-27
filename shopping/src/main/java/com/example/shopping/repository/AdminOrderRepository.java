package com.example.shopping.repository;

import com.example.shopping.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 管理者注文Repositoryインターフェース
 */
public interface AdminOrderRepository {
        Page<Order> findByOrderNumberContainingIgnoreCaseOrderByIdDesc(String orderNumber, Pageable pageable);

        Page<Order> findByCustomerNameContainingIgnoreCaseOrderByIdDesc(String customerName, Pageable pageable);

        Page<Order> findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc(
                        String orderNumber, String customerName, Pageable pageable);

        Page<Order> findByStatusOrderByIdDesc(Order.OrderStatus status, Pageable pageable);

        Page<Order> findByCreatedAtBetweenOrderByIdDesc(LocalDateTime startDate, LocalDateTime endDate,
                        Pageable pageable);

        Page<Order> findAllByOrderByIdDesc(Pageable pageable);

        long countByStatus(Order.OrderStatus status);

        long countTodayOrders(LocalDateTime startOfDay, LocalDateTime endOfDay);

        // 追加
        Optional<Order> findById(Long id);

        Order save(Order order);

        void deleteById(Long id);

        boolean existsById(Long id);

        List<Order> findAll();
}