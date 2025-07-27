package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Product;
import com.example.shopping.repository.UserCartRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserCartRepositoryImpl implements UserCartRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Product> findByIdAndStockQuantityGreaterThan(Long productId, Integer stockQuantity) {
        List<Product> result = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.id = :id AND p.stockQuantity > :stockQuantity", Product.class)
                .setParameter("id", productId)
                .setParameter("stockQuantity", stockQuantity)
                .getResultList();
        return result.stream().findFirst();
    }

    @Override
    public List<Product> findByIdInAndStockQuantityGreaterThanZero(List<Long> productIds) {
        return entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.id IN :ids AND p.stockQuantity > 0", Product.class)
                .setParameter("ids", productIds)
                .getResultList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product);
            return product;
        } else {
            return entityManager.merge(product);
        }
    }

    @Override
    public void deleteById(Long id) {
        Product product = entityManager.find(Product.class, id);
        if (product != null) {
            entityManager.remove(product);
        }
    }

    @Override
    public boolean existsById(Long id) {
        String jpql = "SELECT COUNT(p) FROM Product p WHERE p.id = :id";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }
}