package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.OrderDetail;
import com.example.shopping.repository.UserOrderDetailRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ユーザー注文詳細Repository実装クラス
 */
@Repository
public class UserOrderDetailRepositoryImpl implements UserOrderDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrderDetail> findByOrderIdOrderByIdAsc(Long orderId) {
        return entityManager.createQuery(
                "SELECT od FROM OrderDetail od WHERE od.order.id = :orderId ORDER BY od.id ASC", OrderDetail.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        OrderDetail orderDetail = entityManager.find(OrderDetail.class, id);
        return Optional.ofNullable(orderDetail);
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        if (orderDetail.getId() == null) {
            entityManager.persist(orderDetail);
            return orderDetail;
        } else {
            return entityManager.merge(orderDetail);
        }
    }

    @Override
    public List<OrderDetail> saveAll(List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getId() == null) {
                entityManager.persist(orderDetail);
            } else {
                entityManager.merge(orderDetail);
            }
        }
        return orderDetails;
    }

    @Override
    public void deleteById(Long id) {
        OrderDetail orderDetail = entityManager.find(OrderDetail.class, id);
        if (orderDetail != null) {
            entityManager.remove(orderDetail);
        }
    }

    @Override
    public boolean existsById(Long id) {
        String jpql = "SELECT COUNT(od) FROM OrderDetail od WHERE od.id = :id";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<OrderDetail> findAll() {
        return entityManager.createQuery("SELECT od FROM OrderDetail od", OrderDetail.class)
                .getResultList();
    }
}