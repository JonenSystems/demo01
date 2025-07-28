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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
 * AdminOrderRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class AdminOrderRepositoryImplTest {

        @Mock
        private EntityManager entityManager;

        @Mock
        private TypedQuery<Order> orderQuery;

        @Mock
        private TypedQuery<Long> longQuery;

        @InjectMocks
        private AdminOrderRepositoryImpl adminOrderRepository;

        private Order testOrder1;
        private Order testOrder2;
        private Customer testCustomer;
        private Pageable pageable;

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
                testOrder1.setCreatedAt(LocalDateTime.now());

                testOrder2 = new Order();
                testOrder2.setId(2L);
                testOrder2.setOrderNumber("ORD002");
                testOrder2.setCustomer(testCustomer);
                testOrder2.setTotalAmount(new BigDecimal("3000"));
                testOrder2.setStatus(Order.OrderStatus.SHIPPED);
                testOrder2.setCreatedAt(LocalDateTime.now());

                pageable = PageRequest.of(0, 10);
        }

        @Test
        void UT_Repository_0059_findByOrderNumberContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                List<Order> orderList = Arrays.asList(testOrder1, testOrder2);
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(orderList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(2L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findByOrderNumberContainingIgnoreCaseOrderByIdDesc("ORD001",
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(2, result.getTotalElements());
                assertEquals(1, result.getTotalPages());
                assertEquals("ORD001", result.getContent().get(0).getOrderNumber());
                assertEquals("ORD002", result.getContent().get(1).getOrderNumber());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) ORDER BY o.id DESC",
                                Order.class);
                verify(orderQuery).setParameter("orderNumber", "ORD001");
                verify(orderQuery).setFirstResult(0);
                verify(orderQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%'))",
                                Long.class);
                verify(longQuery).setParameter("orderNumber", "ORD001");
        }

        @Test
        void UT_Repository_0060_findByOrderNumberContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findByOrderNumberContainingIgnoreCaseOrderByIdDesc(
                                "NONEXISTENT",
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) ORDER BY o.id DESC",
                                Order.class);
                verify(orderQuery).setParameter("orderNumber", "NONEXISTENT");
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%'))",
                                Long.class);
                verify(longQuery).setParameter("orderNumber", "NONEXISTENT");
        }

        @Test
        void UT_Repository_0061_findByCustomerNameContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                List<Order> orderList = Arrays.asList(testOrder1, testOrder2);
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(orderList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(2L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findByCustomerNameContainingIgnoreCaseOrderByIdDesc("テスト太郎",
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(2, result.getTotalElements());
                assertEquals(1, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o JOIN o.customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%')) ORDER BY o.id DESC",
                                Order.class);
                verify(orderQuery).setParameter("customerName", "テスト太郎");
                verify(orderQuery).setFirstResult(0);
                verify(orderQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o JOIN o.customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))",
                                Long.class);
                verify(longQuery).setParameter("customerName", "テスト太郎");
        }

        @Test
        void UT_Repository_0062_findByCustomerNameContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findByCustomerNameContainingIgnoreCaseOrderByIdDesc("存在しない顧客",
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o JOIN o.customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%')) ORDER BY o.id DESC",
                                Order.class);
                verify(orderQuery).setParameter("customerName", "存在しない顧客");
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o JOIN o.customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))",
                                Long.class);
                verify(longQuery).setParameter("customerName", "存在しない顧客");
        }

        @Test
        void UT_Repository_0063_findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                List<Order> orderList = Arrays.asList(testOrder1);
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(orderList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(1L);

                // テスト実行
                Page<Order> result = adminOrderRepository
                                .findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc(
                                                "ORD001", "テスト太郎", pageable);

                // 検証
                assertNotNull(result);
                assertEquals(1, result.getContent().size());
                assertEquals(1, result.getTotalElements());
                assertEquals(1, result.getTotalPages());
                assertEquals("ORD001", result.getContent().get(0).getOrderNumber());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o JOIN o.customer c WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) AND LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%')) ORDER BY o.id DESC",
                                Order.class);
                verify(orderQuery).setParameter("orderNumber", "ORD001");
                verify(orderQuery).setParameter("customerName", "テスト太郎");
                verify(orderQuery).setFirstResult(0);
                verify(orderQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o JOIN o.customer c WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) AND LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))",
                                Long.class);
                verify(longQuery).setParameter("orderNumber", "ORD001");
                verify(longQuery).setParameter("customerName", "テスト太郎");
        }

        @Test
        void UT_Repository_0064_findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Order> result = adminOrderRepository
                                .findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc(
                                                "NONEXISTENT", "存在しない顧客", pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o JOIN o.customer c WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) AND LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%')) ORDER BY o.id DESC",
                                Order.class);
                verify(orderQuery).setParameter("orderNumber", "NONEXISTENT");
                verify(orderQuery).setParameter("customerName", "存在しない顧客");
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o JOIN o.customer c WHERE LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%')) AND LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))",
                                Long.class);
                verify(longQuery).setParameter("orderNumber", "NONEXISTENT");
                verify(longQuery).setParameter("customerName", "存在しない顧客");
        }

        @Test
        void UT_Repository_0065_findByStatusOrderByIdDesc_正常系() {
                // モックの設定
                List<Order> orderList = Arrays.asList(testOrder1);
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(orderList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(1L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findByStatusOrderByIdDesc(Order.OrderStatus.PENDING,
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(1, result.getContent().size());
                assertEquals(1, result.getTotalElements());
                assertEquals(1, result.getTotalPages());
                assertEquals(Order.OrderStatus.PENDING, result.getContent().get(0).getStatus());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o WHERE o.status = :status ORDER BY o.id DESC", Order.class);
                verify(orderQuery).setParameter("status", Order.OrderStatus.PENDING);
                verify(orderQuery).setFirstResult(0);
                verify(orderQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.status = :status", Long.class);
                verify(longQuery).setParameter("status", Order.OrderStatus.PENDING);
        }

        @Test
        void UT_Repository_0066_findByStatusOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findByStatusOrderByIdDesc(Order.OrderStatus.PREPARING,
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o WHERE o.status = :status ORDER BY o.id DESC", Order.class);
                verify(orderQuery).setParameter("status", Order.OrderStatus.PREPARING);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.status = :status", Long.class);
                verify(longQuery).setParameter("status", Order.OrderStatus.PREPARING);
        }

        @Test
        void UT_Repository_0067_findByCreatedAtBetweenOrderByIdDesc_正常系() {
                // モックの設定
                List<Order> orderList = Arrays.asList(testOrder1, testOrder2);
                LocalDateTime startDate = LocalDateTime.now().minusDays(7);
                LocalDateTime endDate = LocalDateTime.now();

                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(orderList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(2L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findByCreatedAtBetweenOrderByIdDesc(startDate, endDate,
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(2, result.getTotalElements());
                assertEquals(1, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.id DESC",
                                Order.class);
                verify(orderQuery).setParameter("startDate", startDate);
                verify(orderQuery).setParameter("endDate", endDate);
                verify(orderQuery).setFirstResult(0);
                verify(orderQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate",
                                Long.class);
                verify(longQuery).setParameter("startDate", startDate);
                verify(longQuery).setParameter("endDate", endDate);
        }

        @Test
        void UT_Repository_0068_findByCreatedAtBetweenOrderByIdDesc_正常系() {
                // モックの設定
                LocalDateTime startDate = LocalDateTime.now().minusDays(30);
                LocalDateTime endDate = LocalDateTime.now().minusDays(25);

                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setParameter(anyString(), any())).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findByCreatedAtBetweenOrderByIdDesc(startDate, endDate,
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.id DESC",
                                Order.class);
                verify(orderQuery).setParameter("startDate", startDate);
                verify(orderQuery).setParameter("endDate", endDate);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate",
                                Long.class);
                verify(longQuery).setParameter("startDate", startDate);
                verify(longQuery).setParameter("endDate", endDate);
        }

        @Test
        void UT_Repository_0069_findAllByOrderByIdDesc_正常系() {
                // モックの設定
                List<Order> orderList = Arrays.asList(testOrder1, testOrder2);
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(orderList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(2L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findAllByOrderByIdDesc(pageable);

                // 検証
                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(2, result.getTotalElements());
                assertEquals(1, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o ORDER BY o.id DESC", Order.class);
                verify(orderQuery).setFirstResult(0);
                verify(orderQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o", Long.class);
        }

        @Test
        void UT_Repository_0070_findAllByOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.setFirstResult(anyInt())).thenReturn(orderQuery);
                when(orderQuery.setMaxResults(anyInt())).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Order> result = adminOrderRepository.findAllByOrderByIdDesc(pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o ORDER BY o.id DESC", Order.class);
                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o", Long.class);
        }

        @Test
        void UT_Repository_0071_countByStatus_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(5L);

                // テスト実行
                long result = adminOrderRepository.countByStatus(Order.OrderStatus.PENDING);

                // 検証
                assertEquals(5L, result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.status = :status", Long.class);
                verify(longQuery).setParameter("status", Order.OrderStatus.PENDING);
        }

        @Test
        void UT_Repository_0072_countByStatus_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                long result = adminOrderRepository.countByStatus(Order.OrderStatus.PREPARING);

                // 検証
                assertEquals(0L, result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.status = :status", Long.class);
                verify(longQuery).setParameter("status", Order.OrderStatus.PREPARING);
        }

        @Test
        void UT_Repository_0073_countTodayOrders_正常系() {
                // モックの設定
                LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0);
                LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(3L);

                // テスト実行
                long result = adminOrderRepository.countTodayOrders(startOfDay, endOfDay);

                // 検証
                assertEquals(3L, result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startOfDay AND :endOfDay",
                                Long.class);
                verify(longQuery).setParameter("startOfDay", startOfDay);
                verify(longQuery).setParameter("endOfDay", endOfDay);
        }

        @Test
        void UT_Repository_0074_countTodayOrders_正常系() {
                // モックの設定
                LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0);
                LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                long result = adminOrderRepository.countTodayOrders(startOfDay, endOfDay);

                // 検証
                assertEquals(0L, result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startOfDay AND :endOfDay",
                                Long.class);
                verify(longQuery).setParameter("startOfDay", startOfDay);
                verify(longQuery).setParameter("endOfDay", endOfDay);
        }

        @Test
        void UT_Repository_0075_findById_正常系() {
                // モックの設定
                when(entityManager.find(Order.class, 1L)).thenReturn(testOrder1);

                // テスト実行
                Optional<Order> result = adminOrderRepository.findById(1L);

                // 検証
                assertTrue(result.isPresent());
                assertEquals(1L, result.get().getId());
                assertEquals("ORD001", result.get().getOrderNumber());

                verify(entityManager).find(Order.class, 1L);
        }

        @Test
        void UT_Repository_0076_findById_正常系() {
                // モックの設定
                when(entityManager.find(Order.class, 999L)).thenReturn(null);

                // テスト実行
                Optional<Order> result = adminOrderRepository.findById(999L);

                // 検証
                assertFalse(result.isPresent());

                verify(entityManager).find(Order.class, 999L);
        }

        @Test
        void UT_Repository_0077_save_正常系() {
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
                Order result = adminOrderRepository.save(newOrder);

                // 検証
                assertNotNull(result);
                assertEquals(3L, result.getId());
                assertEquals("ORD003", result.getOrderNumber());

                verify(entityManager).persist(newOrder);
                verify(entityManager, never()).merge(any(Order.class));
        }

        @Test
        void UT_Repository_0078_save_正常系() {
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
                Order result = adminOrderRepository.save(existingOrder);

                // 検証
                assertNotNull(result);
                assertEquals(1L, result.getId());
                assertEquals("ORD001_UPDATED", result.getOrderNumber());

                verify(entityManager).merge(existingOrder);
                verify(entityManager, never()).persist(any(Order.class));
        }

        @Test
        void UT_Repository_0079_deleteById_正常系() {
                // モックの設定
                when(entityManager.find(Order.class, 1L)).thenReturn(testOrder1);

                // テスト実行
                adminOrderRepository.deleteById(1L);

                // 検証
                verify(entityManager).find(Order.class, 1L);
                verify(entityManager).remove(testOrder1);
        }

        @Test
        void UT_Repository_0080_deleteById_正常系() {
                // モックの設定
                when(entityManager.find(Order.class, 999L)).thenReturn(null);

                // テスト実行
                adminOrderRepository.deleteById(999L);

                // 検証
                verify(entityManager).find(Order.class, 999L);
                verify(entityManager, never()).remove(any(Order.class));
        }

        @Test
        void UT_Repository_0081_existsById_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(1L);

                // テスト実行
                boolean result = adminOrderRepository.existsById(1L);

                // 検証
                assertTrue(result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.id = :id", Long.class);
                verify(longQuery).setParameter("id", 1L);
        }

        @Test
        void UT_Repository_0082_existsById_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                boolean result = adminOrderRepository.existsById(999L);

                // 検証
                assertFalse(result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(o) FROM Order o WHERE o.id = :id", Long.class);
                verify(longQuery).setParameter("id", 999L);
        }

        @Test
        void UT_Repository_0083_findAll_正常系() {
                // モックの設定
                List<Order> orderList = Arrays.asList(testOrder1, testOrder2);
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(orderList);

                // テスト実行
                List<Order> result = adminOrderRepository.findAll();

                // 検証
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals("ORD001", result.get(0).getOrderNumber());
                assertEquals("ORD002", result.get(1).getOrderNumber());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o", Order.class);
        }

        @Test
        void UT_Repository_0084_findAll_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(orderQuery);
                when(orderQuery.getResultList()).thenReturn(Collections.emptyList());

                // テスト実行
                List<Order> result = adminOrderRepository.findAll();

                // 検証
                assertNotNull(result);
                assertEquals(0, result.size());

                verify(entityManager).createQuery(
                                "SELECT o FROM Order o", Order.class);
        }
}