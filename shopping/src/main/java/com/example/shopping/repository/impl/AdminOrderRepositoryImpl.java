package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Order;
import com.example.shopping.repository.AdminOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AdminOrderRepositoryImpl implements AdminOrderRepository {
        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public Page<Order> findByOrderNumberContainingIgnoreCaseOrderByIdDesc(String orderNumber, Pageable pageable) {
                List<Order> result = entityManager.createQuery(
                                "SELECT o FROM Order o WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) ORDER BY o.id DESC",
                                Order.class)
                                .setParameter("orderNumber", orderNumber)
                                .setFirstResult((int) pageable.getOffset())
                                .setMaxResults(pageable.getPageSize())
                                .getResultList();
                long total = entityManager.createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%'))",
                                Long.class)
                                .setParameter("orderNumber", orderNumber)
                                .getSingleResult();
                return new PageImpl<>(result, pageable, total);
        }

        @Override
        public Page<Order> findByCustomerNameContainingIgnoreCaseOrderByIdDesc(String customerName, Pageable pageable) {
                List<Order> result = entityManager.createQuery(
                                "SELECT o FROM Order o JOIN o.customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%')) ORDER BY o.id DESC",
                                Order.class)
                                .setParameter("customerName", customerName)
                                .setFirstResult((int) pageable.getOffset())
                                .setMaxResults(pageable.getPageSize())
                                .getResultList();
                long total = entityManager.createQuery(
                                "SELECT COUNT(o) FROM Order o JOIN o.customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))",
                                Long.class)
                                .setParameter("customerName", customerName)
                                .getSingleResult();
                return new PageImpl<>(result, pageable, total);
        }

        @Override
        public Page<Order> findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc(
                        String orderNumber, String customerName, Pageable pageable) {
                List<Order> result = entityManager.createQuery(
                                "SELECT o FROM Order o JOIN o.customer c WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) AND LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%')) ORDER BY o.id DESC",
                                Order.class)
                                .setParameter("orderNumber", orderNumber)
                                .setParameter("customerName", customerName)
                                .setFirstResult((int) pageable.getOffset())
                                .setMaxResults(pageable.getPageSize())
                                .getResultList();
                long total = entityManager.createQuery(
                                "SELECT COUNT(o) FROM Order o JOIN o.customer c WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) AND LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))",
                                Long.class)
                                .setParameter("orderNumber", orderNumber)
                                .setParameter("customerName", customerName)
                                .getSingleResult();
                return new PageImpl<>(result, pageable, total);
        }

        @Override
        public Page<Order> findByStatusOrderByIdDesc(Order.OrderStatus status, Pageable pageable) {
                List<Order> result = entityManager.createQuery(
                                "SELECT o FROM Order o WHERE o.status = :status ORDER BY o.id DESC", Order.class)
                                .setParameter("status", status)
                                .setFirstResult((int) pageable.getOffset())
                                .setMaxResults(pageable.getPageSize())
                                .getResultList();
                long total = entityManager.createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.status = :status", Long.class)
                                .setParameter("status", status)
                                .getSingleResult();
                return new PageImpl<>(result, pageable, total);
        }

        @Override
        public Page<Order> findByCreatedAtBetweenOrderByIdDesc(LocalDateTime startDate, LocalDateTime endDate,
                        Pageable pageable) {
                List<Order> result = entityManager.createQuery(
                                "SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.id DESC",
                                Order.class)
                                .setParameter("startDate", startDate)
                                .setParameter("endDate", endDate)
                                .setFirstResult((int) pageable.getOffset())
                                .setMaxResults(pageable.getPageSize())
                                .getResultList();
                long total = entityManager.createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate",
                                Long.class)
                                .setParameter("startDate", startDate)
                                .setParameter("endDate", endDate)
                                .getSingleResult();
                return new PageImpl<>(result, pageable, total);
        }

        @Override
        public Page<Order> findAllByOrderByIdDesc(Pageable pageable) {
                List<Order> result = entityManager.createQuery(
                                "SELECT o FROM Order o ORDER BY o.id DESC", Order.class)
                                .setFirstResult((int) pageable.getOffset())
                                .setMaxResults(pageable.getPageSize())
                                .getResultList();
                long total = entityManager.createQuery(
                                "SELECT COUNT(o) FROM Order o", Long.class)
                                .getSingleResult();
                return new PageImpl<>(result, pageable, total);
        }

        @Override
        public long countByStatus(Order.OrderStatus status) {
                return entityManager.createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.status = :status", Long.class)
                                .setParameter("status", status)
                                .getSingleResult();
        }

        @Override
        public long countTodayOrders(LocalDateTime startOfDay, LocalDateTime endOfDay) {
                return entityManager.createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startOfDay AND :endOfDay",
                                Long.class)
                                .setParameter("startOfDay", startOfDay)
                                .setParameter("endOfDay", endOfDay)
                                .getSingleResult();
        }

        @Override
        public Optional<Order> findById(Long id) {
                Order order = entityManager.find(Order.class, id);
                return Optional.ofNullable(order);
        }

        @Override
        public Order save(Order order) {
                if (order.getId() == null) {
                        entityManager.persist(order);
                        return order;
                } else {
                        return entityManager.merge(order);
                }
        }

        @Override
        public void deleteById(Long id) {
                Order order = entityManager.find(Order.class, id);
                if (order != null) {
                        entityManager.remove(order);
                }
        }

        @Override
        public boolean existsById(Long id) {
                String jpql = "SELECT COUNT(o) FROM Order o WHERE o.id = :id";
                Long count = entityManager.createQuery(jpql, Long.class)
                                .setParameter("id", id)
                                .getSingleResult();
                return count > 0;
        }

        @Override
        public List<Order> findAll() {
                return entityManager.createQuery("SELECT o FROM Order o", Order.class)
                                .getResultList();
        }
}