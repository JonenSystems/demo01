package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Product;
import com.example.shopping.repository.AdminProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AdminProductRepositoryImpl implements AdminProductRepository {
        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public Page<Product> findByNameContainingIgnoreCaseOrderByIdDesc(String name, Pageable pageable) {
                try {
                        List<Product> result = entityManager.createQuery(
                                        "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER('%' || :name || '%') ORDER BY p.id DESC",
                                        Product.class)
                                        .setParameter("name", name)
                                        .setFirstResult((int) pageable.getOffset())
                                        .setMaxResults(pageable.getPageSize())
                                        .getResultList();
                        long total = entityManager.createQuery(
                                        "SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) LIKE LOWER('%' || :name || '%')",
                                        Long.class)
                                        .setParameter("name", name)
                                        .getSingleResult();
                        return new PageImpl<>(result, pageable, total);
                } catch (Exception e) {
                        return new PageImpl<>(new ArrayList<>(), pageable, 0);
                }
        }

        @Override
        public Page<Product> findByCategoryOrderByIdDesc(String category, Pageable pageable) {
                try {
                        List<Product> result = entityManager.createQuery(
                                        "SELECT p FROM Product p WHERE p.category = :category ORDER BY p.id DESC",
                                        Product.class)
                                        .setParameter("category", category)
                                        .setFirstResult((int) pageable.getOffset())
                                        .setMaxResults(pageable.getPageSize())
                                        .getResultList();
                        long total = entityManager.createQuery(
                                        "SELECT COUNT(p) FROM Product p WHERE p.category = :category", Long.class)
                                        .setParameter("category", category)
                                        .getSingleResult();
                        return new PageImpl<>(result, pageable, total);
                } catch (Exception e) {
                        return new PageImpl<>(new ArrayList<>(), pageable, 0);
                }
        }

        @Override
        public Page<Product> findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc(String name, String category,
                        Pageable pageable) {
                try {
                        List<Product> result = entityManager.createQuery(
                                        "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER('%' || :name || '%') AND p.category = :category ORDER BY p.id DESC",
                                        Product.class)
                                        .setParameter("name", name)
                                        .setParameter("category", category)
                                        .setFirstResult((int) pageable.getOffset())
                                        .setMaxResults(pageable.getPageSize())
                                        .getResultList();
                        long total = entityManager.createQuery(
                                        "SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) LIKE LOWER('%' || :name || '%') AND p.category = :category",
                                        Long.class)
                                        .setParameter("name", name)
                                        .setParameter("category", category)
                                        .getSingleResult();
                        return new PageImpl<>(result, pageable, total);
                } catch (Exception e) {
                        return new PageImpl<>(new ArrayList<>(), pageable, 0);
                }
        }

        @Override
        public List<Product> findByStockQuantityLessThanEqualOrderByIdDesc(Integer stockQuantity) {
                return entityManager.createQuery(
                                "SELECT p FROM Product p WHERE p.stockQuantity <= :stockQuantity ORDER BY p.id DESC",
                                Product.class)
                                .setParameter("stockQuantity", stockQuantity)
                                .getResultList();
        }

        @Override
        public Page<Product> findAllByOrderByIdDesc(Pageable pageable) {
                try {
                        List<Product> result = entityManager.createQuery(
                                        "SELECT p FROM Product p ORDER BY p.id DESC", Product.class)
                                        .setFirstResult((int) pageable.getOffset())
                                        .setMaxResults(pageable.getPageSize())
                                        .getResultList();
                        long total = entityManager.createQuery(
                                        "SELECT COUNT(p) FROM Product p", Long.class)
                                        .getSingleResult();
                        return new PageImpl<>(result, pageable, total);
                } catch (Exception e) {
                        return new PageImpl<>(new ArrayList<>(), pageable, 0);
                }
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
                try {
                        String jpql = "SELECT COUNT(p) FROM Product p WHERE p.id = :id";
                        Long count = entityManager.createQuery(jpql, Long.class)
                                        .setParameter("id", id)
                                        .getSingleResult();
                        return count > 0;
                } catch (Exception e) {
                        return false;
                }
        }

        @Override
        public List<Product> findAll() {
                return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                                .getResultList();
        }

        @Override
        public List<String> findDistinctCategories() {
                try {
                        return entityManager.createQuery(
                                        "SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL AND p.category != '' ORDER BY p.category",
                                        String.class)
                                        .getResultList();
                } catch (Exception e) {
                        // エラーが発生した場合は空のリストを返す
                        return new ArrayList<>();
                }
        }
}