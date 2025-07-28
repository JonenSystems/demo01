package com.example.shopping.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OrderDetailエンティティの単体テスト
 */
class OrderDetailTest {

    private OrderDetail orderDetail;
    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-001");

        product = new Product();
        product.setId(1L);
        product.setName("商品A");
        product.setPrice(new BigDecimal("1000"));

        orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(2);
        orderDetail.setUnitPrice(new BigDecimal("1000"));
        orderDetail.setSubtotal(new BigDecimal("2000"));
    }

    @Test
    void UT_Entity_0010_onCreate_正常系() {
        // テスト実行前の状態確認
        assertNull(orderDetail.getCreatedAt());
        assertNull(orderDetail.getUpdatedAt());

        // テスト実行
        orderDetail.onCreate();

        // 検証
        assertNotNull(orderDetail.getCreatedAt());
        assertNotNull(orderDetail.getUpdatedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(orderDetail.getCreatedAt().isBefore(now.plusSeconds(1)) || 
                   orderDetail.getCreatedAt().isEqual(now));
        assertTrue(orderDetail.getUpdatedAt().isBefore(now.plusSeconds(1)) || 
                   orderDetail.getUpdatedAt().isEqual(now));
    }

    @Test
    void UT_Entity_0011_onUpdate_正常系() {
        // 初期状態設定
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
        orderDetail.setCreatedAt(pastTime);
        orderDetail.setUpdatedAt(pastTime);

        // テスト実行前の状態確認
        assertEquals(pastTime, orderDetail.getCreatedAt());
        assertEquals(pastTime, orderDetail.getUpdatedAt());

        // テスト実行
        orderDetail.onUpdate();

        // 検証
        assertEquals(pastTime, orderDetail.getCreatedAt()); // createdAtは変更されない
        assertNotNull(orderDetail.getUpdatedAt());
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(orderDetail.getUpdatedAt().isAfter(pastTime));
        assertTrue(orderDetail.getUpdatedAt().isBefore(now.plusSeconds(1)) || 
                   orderDetail.getUpdatedAt().isEqual(now));
    }
} 