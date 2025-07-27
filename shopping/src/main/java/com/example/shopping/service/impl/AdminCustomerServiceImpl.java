package com.example.shopping.service.impl;

import com.example.shopping.model.dto.AdminCustomerDto;
import com.example.shopping.model.entity.Customer;
import com.example.shopping.model.form.AdminCustomerListForm;
import com.example.shopping.repository.AdminCustomerRepository;
import com.example.shopping.service.AdminCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理者顧客Service実装クラス
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCustomerServiceImpl implements AdminCustomerService {

    private final AdminCustomerRepository adminCustomerRepository;

    @Override
    public AdminCustomerListForm getCustomerList(String searchName, String searchEmail, Pageable pageable) {
        log.debug("Getting customer list: searchName={}, searchEmail={}, page={}",
                searchName, searchEmail, pageable.getPageNumber());

        Page<Customer> customerPage;

        if (searchName != null && !searchName.trim().isEmpty() &&
                searchEmail != null && !searchEmail.trim().isEmpty()) {
            // 顧客名とメールアドレスで検索
            customerPage = adminCustomerRepository
                    .findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc(
                            searchName.trim(), searchEmail.trim(), pageable);
        } else if (searchName != null && !searchName.trim().isEmpty()) {
            // 顧客名で検索
            customerPage = adminCustomerRepository.findByNameContainingIgnoreCaseOrderByIdDesc(
                    searchName.trim(), pageable);
        } else if (searchEmail != null && !searchEmail.trim().isEmpty()) {
            // メールアドレスで検索
            customerPage = adminCustomerRepository.findByEmailContainingIgnoreCaseOrderByIdDesc(
                    searchEmail.trim(), pageable);
        } else {
            // 全顧客取得
            customerPage = adminCustomerRepository.findAllByOrderByIdDesc(pageable);
        }

        List<AdminCustomerDto> customerDtos = customerPage.getContent().stream()
                .map(AdminCustomerDto::fromEntity)
                .collect(Collectors.toList());

        AdminCustomerListForm form = AdminCustomerListForm.fromDtoList(customerDtos);
        form.setSearchName(searchName);
        form.setSearchEmail(searchEmail);
        form.setCurrentPage(customerPage.getNumber());
        form.setTotalPages(customerPage.getTotalPages());
        form.setTotalElements(customerPage.getTotalElements());
        form.setHasNext(customerPage.hasNext());
        form.setHasPrevious(customerPage.hasPrevious());

        return form;
    }

    @Override
    public AdminCustomerDto getCustomerById(Long customerId) {
        log.debug("Getting customer by id: {}", customerId);
        Customer customer = adminCustomerRepository.findById(customerId).orElse(null);
        return AdminCustomerDto.fromEntity(customer);
    }

    @Override
    @Transactional
    public boolean saveCustomer(AdminCustomerDto customerDto) {
        log.debug("Saving customer: id={}, name={}, email={}",
                customerDto.getId(), customerDto.getName(), customerDto.getEmail());

        try {
            // メールアドレスの重複チェック
            if (isEmailDuplicate(customerDto.getEmail(), customerDto.getId())) {
                log.warn("Email already exists: {}", customerDto.getEmail());
                return false;
            }

            Customer customer = customerDto.toEntity();
            Customer savedCustomer = adminCustomerRepository.save(customer);
            log.debug("Customer saved successfully: id={}", savedCustomer.getId());
            return true;
        } catch (Exception e) {
            log.error("Error saving customer", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteCustomer(Long customerId) {
        log.debug("Deleting customer: id={}", customerId);

        try {
            if (adminCustomerRepository.existsById(customerId)) {
                adminCustomerRepository.deleteById(customerId);
                log.debug("Customer deleted successfully: id={}", customerId);
                return true;
            } else {
                log.warn("Customer not found for deletion: id={}", customerId);
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting customer", e);
            return false;
        }
    }

    @Override
    public boolean isEmailDuplicate(String email, Long excludeId) {
        log.debug("Checking email duplicate: email={}, excludeId={}", email, excludeId);

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        Customer existingCustomer = adminCustomerRepository.findByEmail(email.trim());
        if (existingCustomer == null) {
            return false;
        }

        // 除外IDが指定されている場合、そのIDの顧客は重複としない
        if (excludeId != null && excludeId.equals(existingCustomer.getId())) {
            return false;
        }

        return true;
    }
}