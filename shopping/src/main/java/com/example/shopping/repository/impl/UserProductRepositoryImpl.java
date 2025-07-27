package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Product;
import com.example.shopping.repository.UserProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserProductRepositoryImpl implements UserProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findAllByOrderByIdAsc() {
        return entityManager.createQuery("SELECT p FROM Product p ORDER BY p.id ASC", Product.class)
                .getResultList();
    }

    @Override
    public List<Product> findByCategoryOrderByIdAsc(String category) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.id ASC", Product.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Product> findAvailableProducts() {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.stockQuantity > 0 ORDER BY p.id ASC", Product.class)
                .getResultList();
    }

    @Override
    public List<Product> findAvailableProductsByCategory(String category) {
        return entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.category = :category AND p.stockQuantity > 0 ORDER BY p.id ASC",
                Product.class)
                .setParameter("category", category)
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