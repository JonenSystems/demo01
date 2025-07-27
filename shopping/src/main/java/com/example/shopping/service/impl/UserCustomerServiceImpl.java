package com.example.shopping.service.impl;

import com.example.shopping.model.dto.UserCustomerDto;
import com.example.shopping.model.entity.Customer;
import com.example.shopping.repository.UserCustomerRepository;
import com.example.shopping.service.UserCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * ユーザー顧客Service実装クラス
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserCustomerServiceImpl implements UserCustomerService {

    private final UserCustomerRepository userCustomerRepository;

    @Override
    public boolean saveCustomer(UserCustomerDto customerDto) {
        if (customerDto == null) {
            log.warn("CustomerDto is null");
            return false;
        }

        log.debug("Saving customer: name={}, email={}", customerDto.getName(), customerDto.getEmail());

        // バリデーション
        if (customerDto.getName() == null || customerDto.getName().trim().isEmpty()) {
            log.warn("Customer name is required");
            return false;
        }

        if (customerDto.getEmail() == null || customerDto.getEmail().trim().isEmpty()) {
            log.warn("Customer email is required");
            return false;
        }

        try {
            // データベースに保存
            Customer customer = customerDto.toEntity();
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());

            Customer savedCustomer = userCustomerRepository.save(customer);
            log.debug("Customer saved to database: id={}", savedCustomer.getId());

            log.debug("Customer info saved successfully");
            return true;
        } catch (Exception e) {
            log.error("Error saving customer info", e);
            return false;
        }
    }

    @Override
    public UserCustomerDto getCustomerFromSession() {
        log.debug("Getting customer from session");
        // セッション操作はController層で行うため、ここではビジネスロジックのみ
        return null; // Controller層で実装
    }

    @Override
    public void saveCustomerToSession(UserCustomerDto customerDto) {
        log.debug("Saving customer to session: name={}", customerDto.getName());
        // セッション操作はController層で行うため、ここではビジネスロジックのみ
    }

    @Override
    public void clearCustomerFromSession() {
        log.debug("Clearing customer from session");
        // セッション操作はController層で行うため、ここではビジネスロジックのみ
    }
}