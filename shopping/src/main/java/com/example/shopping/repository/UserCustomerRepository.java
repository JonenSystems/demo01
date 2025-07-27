package com.example.shopping.repository;

import com.example.shopping.model.entity.Customer;
import java.util.Optional;
import java.util.List;

/**
 * ユーザー顧客Repositoryインターフェース
 */
public interface UserCustomerRepository {
    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Customer> findById(Long id);

    Customer save(Customer customer);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Customer> findAll();
}