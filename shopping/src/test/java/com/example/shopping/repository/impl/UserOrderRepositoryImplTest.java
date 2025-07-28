package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Customer;
import com.example.shopping.model.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UserOrderRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class UserOrderRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Order> orderQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    @InjectMocks
    private UserOrderRepositoryImpl userOrderRepository;

    private Order testOrder1;
    private Order testOrder2;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("テスト太郎");
        testCustomer.setEmail("test@example.com");

        testOrder1 = new Order();
        testOrder1.setId(1L);
        testOrder1.setOrderNumber("ORD001");
        testOrder1.setCustomer(testCustomer);
        testOrder1.setTotalAmount(new BigDecimal("5000"));
        testOrder1.setStatus(Order.OrderStatus.PENDING);
        testOrder1.setCreatedAt(LocalDateTime.now().minusDays(1));

        testOrder2 = new Order();
        testOrder2.setId(2L);
        testOrder2.setOrderNumber("ORD002");
        testOrder2.setCustomer(testCustomer);
        testOrder2.setTotalAmount(new BigDecimal("3000"));
        testOrder2.setStatus(Order.OrderStatus.SHIPPED);
        testOrder2.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void UT_Repository_0132_findByOrderNumber_正常系() {
        // モックの設定
        List<Order> orderList = Arrays.asList(testOrder1);
        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
        when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
        when(orderQuery.getResultList()).thenReturn(orderList);

        // テスト実行
        Optional<Order> result = userOrderRepository.findByOrderNumber("ORD001");

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("ORD001", result.get().getOrderNumber());

        verify(entityManager).createQuery(
                "SELECT o FROM Order o WHERE o.orderNumber = :orderNumber", Order.class);
        verify(orderQuery).setParameter("orderNumber", "ORD001");
    }

    @Test
    void UT_Repository_0133_findByOrderNumber_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
        when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
        when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        Optional<Order> result = userOrderRepository.findByOrderNumber("NONEXISTENT");

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery(
                "SELECT o FROM Order o WHERE o.orderNumber = :orderNumber", Order.class);
        verify(orderQuery).setParameter("orderNumber", "NONEXISTENT");
    }

    @Test
    void UT_Repository_0134_findByCustomerIdOrderByCreatedAtDesc_正常系() {
        // モックの設定
        List<Order> orderList = Arrays.asList(testOrder2, testOrder1);
        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
        when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
        when(orderQuery.getResultList()).thenReturn(orderList);

        // テスト実行
        List<Order> result = userOrderRepository.findByCustomerIdOrderByCreatedAtDesc(1L);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId()); // 新しい注文が先
        assertEquals(1L, result.get(1).getId()); // 古い注文が後

        verify(entityManager).createQuery(
                "SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC", Order.class);
        verify(orderQuery).setParameter("customerId", 1L);
    }

    @Test
    void UT_Repository_0135_findByCustomerIdOrderByCreatedAtDesc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
        when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
        when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Order> result = userOrderRepository.findByCustomerIdOrderByCreatedAtDesc(999L);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC", Order.class);
        verify(orderQuery).setParameter("customerId", 999L);
    }

    @Test
    void UT_Repository_0136_findById_正常系() {
        // モックの設定
        when(entityManager.find(Order.class, 1L)).thenReturn(testOrder1);

        // テスト実行
        Optional<Order> result = userOrderRepository.findById(1L);

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("ORD001", result.get().getOrderNumber());

        verify(entityManager).find(Order.class, 1L);
    }

    @Test
    void UT_Repository_0137_findById_正常系() {
        // モックの設定
        when(entityManager.find(Order.class, 999L)).thenReturn(null);

        // テスト実行
        Optional<Order> result = userOrderRepository.findById(999L);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).find(Order.class, 999L);
    }

    @Test
    void UT_Repository_0138_save_正常系() {
        // 新規注文の作成
        Order newOrder = new Order();
        newOrder.setOrderNumber("ORD003");
        newOrder.setCustomer(testCustomer);
        newOrder.setTotalAmount(new BigDecimal("4000"));
        newOrder.setStatus(Order.OrderStatus.PENDING);

        // モックの設定
        doAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(3L);
            return null;
        }).when(entityManager).persist(any(Order.class));

        // テスト実行
        Order result = userOrderRepository.save(newOrder);

        // 検証
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("ORD003", result.getOrderNumber());

        verify(entityManager).persist(newOrder);
        verify(entityManager, never()).merge(any(Order.class));
    }

    @Test
    void UT_Repository_0139_save_正常系() {
        // 既存注文の更新
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setOrderNumber("ORD001_UPDATED");
        existingOrder.setCustomer(testCustomer);
        existingOrder.setTotalAmount(new BigDecimal("6000"));
        existingOrder.setStatus(Order.OrderStatus.SHIPPED);

        // モックの設定
        when(entityManager.merge(existingOrder)).thenReturn(existingOrder);

        // テスト実行
        Order result = userOrderRepository.save(existingOrder);

        // 検証
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ORD001_UPDATED", result.getOrderNumber());

        verify(entityManager).merge(existingOrder);
        verify(entityManager, never()).persist(any(Order.class));
    }

    @Test
    void UT_Repository_0140_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Order.class, 1L)).thenReturn(testOrder1);

        // テスト実行
        userOrderRepository.deleteById(1L);

        // 検証
        verify(entityManager).find(Order.class, 1L);
        verify(entityManager).remove(testOrder1);
    }

    @Test
    void UT_Repository_0141_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Order.class, 999L)).thenReturn(null);

        // テスト実行
        userOrderRepository.deleteById(999L);

        // 検証
        verify(entityManager).find(Order.class, 999L);
        verify(entityManager, never()).remove(any(Order.class));
    }

    @Test
    void UT_Repository_0142_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = userOrderRepository.existsById(1L);

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(o) FROM Order o WHERE o.id = :id", Long.class);
        verify(longQuery).setParameter("id", 1L);
    }

    @Test
    void UT_Repository_0143_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = userOrderRepository.existsById(999L);

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(o) FROM Order o WHERE o.id = :id", Long.class);
        verify(longQuery).setParameter("id", 999L);
    }

    @Test
    void UT_Repository_0144_findAll_正常系() {
        // モックの設定
        List<Order> orderList = Arrays.asList(testOrder1, testOrder2);
        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
        when(orderQuery.getResultList()).thenReturn(orderList);

        // テスト実行
        List<Order> result = userOrderRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORD001", result.get(0).getOrderNumber());
        assertEquals("ORD002", result.get(1).getOrderNumber());

        verify(entityManager).createQuery(
                "SELECT o FROM Order o", Order.class);
    }

    @Test
    void UT_Repository_0145_findAll_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
        when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Order> result = userOrderRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT o FROM Order o", Order.class);
    }
}