package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Customer;
import com.example.shopping.repository.UserCustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserCustomerRepositoryImpl implements UserCustomerRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Customer> findByEmail(String email) {
        List<Customer> result = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                .setParameter("email", email)
                .getResultList();
        return result.stream().findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.email = :email", Long.class)
                .setParameter("email", email)
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