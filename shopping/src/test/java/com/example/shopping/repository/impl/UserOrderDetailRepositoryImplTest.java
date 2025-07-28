package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Order;
import com.example.shopping.model.entity.OrderDetail;
import com.example.shopping.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UserOrderDetailRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class UserOrderDetailRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<OrderDetail> orderDetailQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    @InjectMocks
    private UserOrderDetailRepositoryImpl userOrderDetailRepository;

    private OrderDetail testOrderDetail1;
    private OrderDetail testOrderDetail2;
    private Order testOrder;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD001");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("テスト商品");
        testProduct.setPrice(new BigDecimal("1000"));

        testOrderDetail1 = new OrderDetail();
        testOrderDetail1.setId(1L);
        testOrderDetail1.setOrder(testOrder);
        testOrderDetail1.setProduct(testProduct);
        testOrderDetail1.setQuantity(2);
        testOrderDetail1.setUnitPrice(new BigDecimal("1000"));
        testOrderDetail1.setSubtotal(new BigDecimal("2000"));

        testOrderDetail2 = new OrderDetail();
        testOrderDetail2.setId(2L);
        testOrderDetail2.setOrder(testOrder);
        testOrderDetail2.setProduct(testProduct);
        testOrderDetail2.setQuantity(1);
        testOrderDetail2.setUnitPrice(new BigDecimal("2000"));
        testOrderDetail2.setSubtotal(new BigDecimal("2000"));
    }

    @Test
    void UT_Repository_0146_findByOrderIdOrderByIdAsc_正常系() {
        // モックの設定
        List<OrderDetail> orderDetailList = Arrays.asList(testOrderDetail1, testOrderDetail2);
        when(entityManager.createQuery(anyString(), eq(OrderDetail.class))).thenReturn(orderDetailQuery);
        when(orderDetailQuery.setParameter(anyString(), any())).thenReturn(orderDetailQuery);
        when(orderDetailQuery.getResultList()).thenReturn(orderDetailList);

        // テスト実行
        List<OrderDetail> result = userOrderDetailRepository.findByOrderIdOrderByIdAsc(1L);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(entityManager).createQuery(
                "SELECT od FROM OrderDetail od WHERE od.order.id = :orderId ORDER BY od.id ASC", OrderDetail.class);
        verify(orderDetailQuery).setParameter("orderId", 1L);
    }

    @Test
    void UT_Repository_0147_findByOrderIdOrderByIdAsc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(OrderDetail.class))).thenReturn(orderDetailQuery);
        when(orderDetailQuery.setParameter(anyString(), any())).thenReturn(orderDetailQuery);
        when(orderDetailQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<OrderDetail> result = userOrderDetailRepository.findByOrderIdOrderByIdAsc(999L);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT od FROM OrderDetail od WHERE od.order.id = :orderId ORDER BY od.id ASC", OrderDetail.class);
        verify(orderDetailQuery).setParameter("orderId", 999L);
    }

    @Test
    void UT_Repository_0148_findById_正常系() {
        // モックの設定
        when(entityManager.find(OrderDetail.class, 1L)).thenReturn(testOrderDetail1);

        // テスト実行
        Optional<OrderDetail> result = userOrderDetailRepository.findById(1L);

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(2, result.get().getQuantity());

        verify(entityManager).find(OrderDetail.class, 1L);
    }

    @Test
    void UT_Repository_0149_findById_正常系() {
        // モックの設定
        when(entityManager.find(OrderDetail.class, 999L)).thenReturn(null);

        // テスト実行
        Optional<OrderDetail> result = userOrderDetailRepository.findById(999L);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).find(OrderDetail.class, 999L);
    }

    @Test
    void UT_Repository_0150_save_正常系() {
        // 新規注文詳細の作成
        OrderDetail newOrderDetail = new OrderDetail();
        newOrderDetail.setOrder(testOrder);
        newOrderDetail.setProduct(testProduct);
        newOrderDetail.setQuantity(3);
        newOrderDetail.setUnitPrice(new BigDecimal("1500"));
        newOrderDetail.setSubtotal(new BigDecimal("4500"));

        // モックの設定
        doAnswer(invocation -> {
            OrderDetail orderDetail = invocation.getArgument(0);
            orderDetail.setId(3L);
            return null;
        }).when(entityManager).persist(any(OrderDetail.class));

        // テスト実行
        OrderDetail result = userOrderDetailRepository.save(newOrderDetail);

        // 検証
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals(3, result.getQuantity());

        verify(entityManager).persist(newOrderDetail);
        verify(entityManager, never()).merge(any(OrderDetail.class));
    }

    @Test
    void UT_Repository_0151_save_正常系() {
        // 既存注文詳細の更新
        OrderDetail existingOrderDetail = new OrderDetail();
        existingOrderDetail.setId(1L);
        existingOrderDetail.setOrder(testOrder);
        existingOrderDetail.setProduct(testProduct);
        existingOrderDetail.setQuantity(5);
        existingOrderDetail.setUnitPrice(new BigDecimal("2500"));
        existingOrderDetail.setSubtotal(new BigDecimal("12500"));

        // モックの設定
        when(entityManager.merge(existingOrderDetail)).thenReturn(existingOrderDetail);

        // テスト実行
        OrderDetail result = userOrderDetailRepository.save(existingOrderDetail);

        // 検証
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getQuantity());

        verify(entityManager).merge(existingOrderDetail);
        verify(entityManager, never()).persist(any(OrderDetail.class));
    }

    @Test
    void UT_Repository_0152_saveAll_正常系() {
        // 複数の注文詳細を作成
        OrderDetail newOrderDetail1 = new OrderDetail();
        newOrderDetail1.setOrder(testOrder);
        newOrderDetail1.setProduct(testProduct);
        newOrderDetail1.setQuantity(1);
        newOrderDetail1.setUnitPrice(new BigDecimal("1000"));
        newOrderDetail1.setSubtotal(new BigDecimal("1000"));

        OrderDetail newOrderDetail2 = new OrderDetail();
        newOrderDetail2.setOrder(testOrder);
        newOrderDetail2.setProduct(testProduct);
        newOrderDetail2.setQuantity(2);
        newOrderDetail2.setUnitPrice(new BigDecimal("2000"));
        newOrderDetail2.setSubtotal(new BigDecimal("4000"));

        List<OrderDetail> orderDetails = Arrays.asList(newOrderDetail1, newOrderDetail2);

        // モックの設定
        doAnswer(invocation -> {
            OrderDetail orderDetail = invocation.getArgument(0);
            if (orderDetail.getId() == null) {
                orderDetail.setId(3L);
            }
            return null;
        }).when(entityManager).persist(any(OrderDetail.class));

        // テスト実行
        List<OrderDetail> result = userOrderDetailRepository.saveAll(orderDetails);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(3L, result.get(0).getId());
        assertEquals(3L, result.get(1).getId());

        verify(entityManager, times(2)).persist(any(OrderDetail.class));
        verify(entityManager, never()).merge(any(OrderDetail.class));
    }

    @Test
    void UT_Repository_0153_saveAll_正常系() {
        // 空のリスト
        List<OrderDetail> orderDetails = Collections.emptyList();

        // テスト実行
        List<OrderDetail> result = userOrderDetailRepository.saveAll(orderDetails);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager, never()).persist(any(OrderDetail.class));
        verify(entityManager, never()).merge(any(OrderDetail.class));
    }

    @Test
    void UT_Repository_0154_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(OrderDetail.class, 1L)).thenReturn(testOrderDetail1);

        // テスト実行
        userOrderDetailRepository.deleteById(1L);

        // 検証
        verify(entityManager).find(OrderDetail.class, 1L);
        verify(entityManager).remove(testOrderDetail1);
    }

    @Test
    void UT_Repository_0155_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(OrderDetail.class, 999L)).thenReturn(null);

        // テスト実行
        userOrderDetailRepository.deleteById(999L);

        // 検証
        verify(entityManager).find(OrderDetail.class, 999L);
        verify(entityManager, never()).remove(any(OrderDetail.class));
    }

    @Test
    void UT_Repository_0156_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = userOrderDetailRepository.existsById(1L);

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(od) FROM OrderDetail od WHERE od.id = :id", Long.class);
        verify(longQuery).setParameter("id", 1L);
    }

    @Test
    void UT_Repository_0157_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = userOrderDetailRepository.existsById(999L);

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(od) FROM OrderDetail od WHERE od.id = :id", Long.class);
        verify(longQuery).setParameter("id", 999L);
    }

    @Test
    void UT_Repository_0158_findAll_正常系() {
        // モックの設定
        List<OrderDetail> orderDetailList = Arrays.asList(testOrderDetail1, testOrderDetail2);
        when(entityManager.createQuery(anyString(), eq(OrderDetail.class))).thenReturn(orderDetailQuery);
        when(orderDetailQuery.getResultList()).thenReturn(orderDetailList);

        // テスト実行
        List<OrderDetail> result = userOrderDetailRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(entityManager).createQuery(
                "SELECT od FROM OrderDetail od", OrderDetail.class);
    }

    @Test
    void UT_Repository_0159_findAll_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(OrderDetail.class))).thenReturn(orderDetailQuery);
        when(orderDetailQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<OrderDetail> result = userOrderDetailRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT od FROM OrderDetail od", OrderDetail.class);
    }
}