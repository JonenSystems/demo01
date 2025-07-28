package com.example.shopping.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Customerエンティティの単体テスト
 */
class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("顧客A");
        customer.setEmail("customer@example.com");
    }

    @Test
    void UT_Entity_0003_onCreate_正常系() {
        // テスト実行前の状態確認
        assertNull(customer.getCreatedAt());
        assertNull(customer.getUpdatedAt());

        // テスト実行
        customer.onCreate();

        // 検証
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(customer.getCreatedAt().isBefore(now.plusSeconds(1)) || 
                   customer.getCreatedAt().isEqual(now));
        assertTrue(customer.getUpdatedAt().isBefore(now.plusSeconds(1)) || 
                   customer.getUpdatedAt().isEqual(now));
    }

    @Test
    void UT_Entity_0004_onUpdate_正常系() {
        // 初期状態設定
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
        customer.setCreatedAt(pastTime);
        customer.setUpdatedAt(pastTime);

        // テスト実行前の状態確認
        assertEquals(pastTime, customer.getCreatedAt());
        assertEquals(pastTime, customer.getUpdatedAt());

        // テスト実行
        customer.onUpdate();

        // 検証
        assertEquals(pastTime, customer.getCreatedAt()); // createdAtは変更されない
        assertNotNull(customer.getUpdatedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(customer.getUpdatedAt().isAfter(pastTime));
        assertTrue(customer.getUpdatedAt().isBefore(now.plusSeconds(1)) || 
                   customer.getUpdatedAt().isEqual(now));
    }
} 