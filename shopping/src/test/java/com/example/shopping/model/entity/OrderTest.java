package com.example.shopping.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Orderエンティティの単体テスト
 */
class OrderTest {

    private Order order;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("顧客A");
        customer.setEmail("customer@example.com");

        order = new Order();
        order.setOrderNumber("ORD-001");
        order.setCustomer(customer);
        order.setTotalAmount(new BigDecimal("3000"));
        order.setStatus(Order.OrderStatus.PENDING);
    }

    @Test
    void UT_Entity_0005_onCreate_正常系() {
        // テスト実行前の状態確認
        assertNull(order.getCreatedAt());
        assertNull(order.getUpdatedAt());

        // テスト実行
        order.onCreate();

        // 検証
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(order.getCreatedAt().isBefore(now.plusSeconds(1)) || 
                   order.getCreatedAt().isEqual(now));
        assertTrue(order.getUpdatedAt().isBefore(now.plusSeconds(1)) || 
                   order.getUpdatedAt().isEqual(now));
    }

    @Test
    void UT_Entity_0006_onUpdate_正常系() {
        // 初期状態設定
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
        order.setCreatedAt(pastTime);
        order.setUpdatedAt(pastTime);

        // テスト実行前の状態確認
        assertEquals(pastTime, order.getCreatedAt());
        assertEquals(pastTime, order.getUpdatedAt());

        // テスト実行
        order.onUpdate();

        // 検証
        assertEquals(pastTime, order.getCreatedAt()); // createdAtは変更されない
        assertNotNull(order.getUpdatedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(order.getUpdatedAt().isAfter(pastTime));
        assertTrue(order.getUpdatedAt().isBefore(now.plusSeconds(1)) || 
                   order.getUpdatedAt().isEqual(now));
    }

    @Test
    void UT_Entity_0007_getDisplayName_正常系() {
        // テスト実行
        String displayName = Order.OrderStatus.PENDING.getDisplayName();

        // 検証
        assertEquals("納期確認中", displayName);
    }

    @Test
    void UT_Entity_0008_getDisplayName_正常系() {
        // テスト実行
        String displayName = Order.OrderStatus.PREPARING.getDisplayName();

        // 検証
        assertEquals("発送準備中", displayName);
    }

    @Test
    void UT_Entity_0009_getDisplayName_正常系() {
        // テスト実行
        String displayName = Order.OrderStatus.SHIPPED.getDisplayName();

        // 検証
        assertEquals("発送済み", displayName);
    }
} 