package com.example.shopping.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Productエンティティの単体テスト
 */
class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("商品A");
        product.setPrice(new BigDecimal("1000"));
        product.setStockQuantity(10);
    }

    @Test
    void UT_Entity_0001_onCreate_正常系() {
        // テスト実行前の状態確認
        assertNull(product.getCreatedAt());
        assertNull(product.getUpdatedAt());

        // テスト実行
        product.onCreate();

        // 検証
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(product.getCreatedAt().isBefore(now.plusSeconds(1)) || 
                   product.getCreatedAt().isEqual(now));
        assertTrue(product.getUpdatedAt().isBefore(now.plusSeconds(1)) || 
                   product.getUpdatedAt().isEqual(now));
    }

    @Test
    void UT_Entity_0002_onUpdate_正常系() {
        // 初期状態設定
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
        product.setCreatedAt(pastTime);
        product.setUpdatedAt(pastTime);

        // テスト実行前の状態確認
        assertEquals(pastTime, product.getCreatedAt());
        assertEquals(pastTime, product.getUpdatedAt());

        // テスト実行
        product.onUpdate();

        // 検証
        assertEquals(pastTime, product.getCreatedAt()); // createdAtは変更されない
        assertNotNull(product.getUpdatedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(product.getUpdatedAt().isAfter(pastTime));
        assertTrue(product.getUpdatedAt().isBefore(now.plusSeconds(1)) || 
                   product.getUpdatedAt().isEqual(now));
    }
} 