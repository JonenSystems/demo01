package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Customer;
import com.example.shopping.repository.AdminCustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdminCustomerRepositoryImpl implements AdminCustomerRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Customer> findByNameContainingIgnoreCaseOrderByIdDesc(String name, Pageable pageable) {
        List<Customer> result = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY c.id DESC",
                Customer.class)
                .setParameter("name", name)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        long total = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))", Long.class)
                .setParameter("name", name)
                .getSingleResult();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Customer> findByEmailContainingIgnoreCaseOrderByIdDesc(String email, Pageable pageable) {
        List<Customer> result = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')) ORDER BY c.id DESC",
                Customer.class)
                .setParameter("email", email)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        long total = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Customer> findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc(String name,
            String email, Pageable pageable) {
        List<Customer> result = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')) ORDER BY c.id DESC",
                Customer.class)
                .setParameter("name", name)
                .setParameter("email", email)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        long total = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))",
                Long.class)
                .setParameter("name", name)
                .setParameter("email", email)
                .getSingleResult();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Customer> findAllByOrderByIdDesc(Pageable pageable) {
        List<Customer> result = entityManager.createQuery(
                "SELECT c FROM Customer c ORDER BY c.id DESC", Customer.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        long total = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c", Long.class)
                .getSingleResult();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Customer findByEmail(String email) {
        List<Customer> result = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                .setParameter("email", email)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public boolean existsByEmailExcludingId(String email, Long excludeId) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.email = :email AND c.id != :excludeId", Long.class)
                .setParameter("email", email)
                .setParameter("excludeId", excludeId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        return Optional.ofNullable(customer);
    }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            entityManager.persist(customer);
            return customer;
        } else {
            return entityManager.merge(customer);
        }
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }

    @Override
    public boolean existsById(Long id) {
        String jpql = "SELECT COUNT(c) FROM Customer c WHERE c.id = :id";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<Customer> findAll() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }
}