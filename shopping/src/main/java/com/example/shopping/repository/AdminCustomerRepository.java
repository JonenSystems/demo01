package com.example.shopping.repository;

import com.example.shopping.model.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * 管理者顧客Repositoryインターフェース
 */
public interface AdminCustomerRepository {
    Page<Customer> findByNameContainingIgnoreCaseOrderByIdDesc(String name, Pageable pageable);

    Page<Customer> findByEmailContainingIgnoreCaseOrderByIdDesc(String email, Pageable pageable);

    Page<Customer> findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc(String name, String email,
            Pageable pageable);

    Page<Customer> findAllByOrderByIdDesc(Pageable pageable);

    Customer findByEmail(String email);

    boolean existsByEmailExcludingId(String email, Long excludeId);

    // 追加
    Optional<Customer> findById(Long id);

    Customer save(Customer customer);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Customer> findAll();
}