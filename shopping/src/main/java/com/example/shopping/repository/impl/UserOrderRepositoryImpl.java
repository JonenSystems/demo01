package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Order;
import com.example.shopping.repository.UserOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserOrderRepositoryImpl implements UserOrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        List<Order> result = entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.orderNumber = :orderNumber", Order.class)
                .setParameter("orderNumber", orderNumber)
                .getResultList();
        return result.stream().findFirst();
    }

    @Override
    public List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId) {
        return entityManager.createQuery(
                "SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC", Order.class)
                .setParameter("customerId", customerId)
                .getResultList();
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