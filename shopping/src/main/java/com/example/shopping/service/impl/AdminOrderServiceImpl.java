package com.example.shopping.service.impl;

import com.example.shopping.model.dto.AdminOrderDto;
import com.example.shopping.model.entity.Order;
import com.example.shopping.model.form.AdminOrderListForm;
import com.example.shopping.repository.AdminOrderRepository;
import com.example.shopping.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理者注文Service実装クラス
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminOrderServiceImpl implements AdminOrderService {

    private final AdminOrderRepository adminOrderRepository;

    @Override
    public AdminOrderListForm getOrderList(String searchOrderNumber, String searchCustomerName, String searchStatus,
            Pageable pageable) {
        log.debug("Getting order list: searchOrderNumber={}, searchCustomerName={}, searchStatus={}, page={}",
                searchOrderNumber, searchCustomerName, searchStatus, pageable.getPageNumber());

        Page<Order> orderPage;

        if (searchOrderNumber != null && !searchOrderNumber.trim().isEmpty() &&
                searchCustomerName != null && !searchCustomerName.trim().isEmpty()) {
            // 注文番号と顧客名で検索
            orderPage = adminOrderRepository
                    .findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc(
                            searchOrderNumber.trim(), searchCustomerName.trim(), pageable);
        } else if (searchOrderNumber != null && !searchOrderNumber.trim().isEmpty()) {
            // 注文番号で検索
            orderPage = adminOrderRepository.findByOrderNumberContainingIgnoreCaseOrderByIdDesc(
                    searchOrderNumber.trim(), pageable);
        } else if (searchCustomerName != null && !searchCustomerName.trim().isEmpty()) {
            // 顧客名で検索
            orderPage = adminOrderRepository.findByCustomerNameContainingIgnoreCaseOrderByIdDesc(
                    searchCustomerName.trim(), pageable);
        } else if (searchStatus != null && !searchStatus.trim().isEmpty()) {
            // ステータスで検索
            try {
                Order.OrderStatus status = Order.OrderStatus.valueOf(searchStatus.trim());
                orderPage = adminOrderRepository.findByStatusOrderByIdDesc(status, pageable);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status: {}", searchStatus);
                orderPage = adminOrderRepository.findAllByOrderByIdDesc(pageable);
            }
        } else {
            // 全注文取得
            orderPage = adminOrderRepository.findAllByOrderByIdDesc(pageable);
        }

        List<AdminOrderDto> orderDtos = orderPage.getContent() != null ? orderPage.getContent().stream()
                .map(AdminOrderDto::fromEntity)
                .collect(Collectors.toList()) : new ArrayList<>();

        AdminOrderListForm form = AdminOrderListForm.fromDtoList(orderDtos);
        form.setSearchOrderNumber(searchOrderNumber);
        form.setSearchCustomerName(searchCustomerName);
        form.setSearchStatus(searchStatus);
        form.setStatusOptions(getAvailableStatuses());
        form.setCurrentPage(orderPage.getNumber());
        form.setTotalPages(orderPage.getTotalPages());
        form.setTotalElements(orderPage.getTotalElements());
        form.setHasNext(orderPage.hasNext());
        form.setHasPrevious(orderPage.hasPrevious());

        return form;
    }

    @Override
    public AdminOrderDto getOrderById(Long orderId) {
        log.debug("Getting order by id: {}", orderId);
        Order order = adminOrderRepository.findById(orderId).orElse(null);
        return AdminOrderDto.fromEntity(order);
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long orderId, String status) {
        log.debug("Updating order status: orderId={}, status={}", orderId, status);

        try {
            Order order = adminOrderRepository.findById(orderId).orElse(null);
            if (order == null) {
                log.warn("Order not found: orderId={}", orderId);
                return false;
            }

            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status);
            order.setStatus(newStatus);
            adminOrderRepository.save(order);

            log.debug("Order status updated successfully: orderId={}, status={}", orderId, status);
            return true;
        } catch (Exception e) {
            log.error("Error updating order status", e);
            return false;
        }
    }

    @Override
    public List<String> getAvailableStatuses() {
        return Arrays.stream(Order.OrderStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getOrderStatistics() {
        log.debug("Getting order statistics");
        Map<String, Object> statistics = new HashMap<>();

        try {
            // 各ステータスの注文数を取得
            for (Order.OrderStatus status : Order.OrderStatus.values()) {
                log.debug("Counting orders with status: {}", status);
                long count = adminOrderRepository.countByStatus(status);
                statistics.put(status.name() + "_COUNT", count);
                log.debug("Count for {}: {}", status, count);
            }

            // 今日の注文数を取得
            LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            log.debug("Counting today's orders: {} to {}", startOfDay, endOfDay);
            long todayCount = adminOrderRepository.countTodayOrders(startOfDay, endOfDay);
            statistics.put("TODAY_COUNT", todayCount);
            log.debug("Today's count: {}", todayCount);

            log.debug("Order statistics completed: {}", statistics);
            return statistics;
        } catch (Exception e) {
            log.error("Error getting order statistics: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    @Override
    public long getTodayOrderCount() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return adminOrderRepository.countTodayOrders(startOfDay, endOfDay);
    }
}