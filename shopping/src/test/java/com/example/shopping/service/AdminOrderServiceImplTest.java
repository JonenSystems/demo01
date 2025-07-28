package com.example.shopping.service;

import com.example.shopping.model.dto.AdminOrderDto;
import com.example.shopping.model.entity.Customer;
import com.example.shopping.model.entity.Order;
import com.example.shopping.model.form.AdminOrderListForm;
import com.example.shopping.repository.AdminOrderRepository;
import com.example.shopping.service.impl.AdminOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 管理者注文サービスのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminOrderServiceImplTest {

    @Mock
    private AdminOrderRepository adminOrderRepository;

    @InjectMocks
    private AdminOrderServiceImpl adminOrderService;

    private Order testOrder1;
    private Order testOrder2;
    private Order testOrder3;
    private Customer testCustomer1;
    private Customer testCustomer2;

    @BeforeEach
    void setUp() {
        // テスト用の顧客1
        testCustomer1 = new Customer();
        testCustomer1.setId(1L);
        testCustomer1.setName("田中太郎");
        testCustomer1.setEmail("tanaka@example.com");
        testCustomer1.setPhone("090-1234-5678");
        testCustomer1.setAddress("東京都渋谷区1-1-1");
        testCustomer1.setCreatedAt(LocalDateTime.now());
        testCustomer1.setUpdatedAt(LocalDateTime.now());

        // テスト用の顧客2
        testCustomer2 = new Customer();
        testCustomer2.setId(2L);
        testCustomer2.setName("佐藤花子");
        testCustomer2.setEmail("sato@example.com");
        testCustomer2.setPhone("090-9876-5432");
        testCustomer2.setAddress("大阪府大阪市2-2-2");
        testCustomer2.setCreatedAt(LocalDateTime.now());
        testCustomer2.setUpdatedAt(LocalDateTime.now());

        // テスト用の注文1
        testOrder1 = new Order();
        testOrder1.setId(1L);
        testOrder1.setOrderNumber("ORD-001");
        testOrder1.setCustomer(testCustomer1);
        testOrder1.setTotalAmount(new BigDecimal("15000"));
        testOrder1.setStatus(Order.OrderStatus.PENDING);
        testOrder1.setCreatedAt(LocalDateTime.now());
        testOrder1.setUpdatedAt(LocalDateTime.now());

        // テスト用の注文2
        testOrder2 = new Order();
        testOrder2.setId(2L);
        testOrder2.setOrderNumber("ORD-002");
        testOrder2.setCustomer(testCustomer2);
        testOrder2.setTotalAmount(new BigDecimal("25000"));
        testOrder2.setStatus(Order.OrderStatus.SHIPPED);
        testOrder2.setCreatedAt(LocalDateTime.now());
        testOrder2.setUpdatedAt(LocalDateTime.now());

        // テスト用の注文3
        testOrder3 = new Order();
        testOrder3.setId(3L);
        testOrder3.setOrderNumber("ORD-003");
        testOrder3.setCustomer(testCustomer1);
        testOrder3.setTotalAmount(new BigDecimal("35000"));
        testOrder3.setStatus(Order.OrderStatus.SHIPPED);
        testOrder3.setCreatedAt(LocalDateTime.now());
        testOrder3.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void UT_Service_0061_getOrderList_正常系() {
        // 検索条件なしで全注文取得
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = Arrays.asList(testOrder1, testOrder2, testOrder3);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, 3);

        when(adminOrderRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(orderPage);

        AdminOrderListForm result = adminOrderService.getOrderList(null, null, null, pageable);

        assertNotNull(result);
        assertEquals(3, result.getOrders().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertNull(result.getSearchOrderNumber());
        assertNull(result.getSearchCustomerName());
        assertNull(result.getSearchStatus());
        assertNotNull(result.getStatusOptions());

        verify(adminOrderRepository, times(1)).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0062_getOrderList_正常系() {
        // 注文番号で検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = Arrays.asList(testOrder1);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, 1);

        when(adminOrderRepository.findByOrderNumberContainingIgnoreCaseOrderByIdDesc("ORD-001", pageable))
                .thenReturn(orderPage);

        AdminOrderListForm result = adminOrderService.getOrderList("ORD-001", null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        assertEquals("ORD-001", result.getOrders().get(0).getOrderNumber());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        assertEquals("ORD-001", result.getSearchOrderNumber());

        verify(adminOrderRepository, times(1)).findByOrderNumberContainingIgnoreCaseOrderByIdDesc("ORD-001", pageable);
    }

    @Test
    void UT_Service_0063_getOrderList_正常系() {
        // 顧客名で検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = Arrays.asList(testOrder1, testOrder3);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, 2);

        when(adminOrderRepository.findByCustomerNameContainingIgnoreCaseOrderByIdDesc("田中", pageable))
                .thenReturn(orderPage);

        AdminOrderListForm result = adminOrderService.getOrderList(null, "田中", null, pageable);

        assertNotNull(result);
        assertEquals(2, result.getOrders().size());
        assertTrue(result.getOrders().stream().allMatch(order -> order.getCustomer().getName().contains("田中")));
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertEquals("田中", result.getSearchCustomerName());

        verify(adminOrderRepository, times(1)).findByCustomerNameContainingIgnoreCaseOrderByIdDesc("田中", pageable);
    }

    @Test
    void UT_Service_0064_getOrderList_正常系() {
        // ステータスで検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = Arrays.asList(testOrder1);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, 1);

        when(adminOrderRepository.findByStatusOrderByIdDesc(Order.OrderStatus.PENDING, pageable))
                .thenReturn(orderPage);

        AdminOrderListForm result = adminOrderService.getOrderList(null, null, "PENDING", pageable);

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        assertEquals(Order.OrderStatus.PENDING, result.getOrders().get(0).getStatus());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        assertEquals("PENDING", result.getSearchStatus());

        verify(adminOrderRepository, times(1)).findByStatusOrderByIdDesc(Order.OrderStatus.PENDING, pageable);
    }

    @Test
    void UT_Service_0065_getOrderList_正常系() {
        // 複数条件で検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = Arrays.asList(testOrder1);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, 1);

        when(adminOrderRepository.findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc(
                "ORD", "田中", pageable))
                .thenReturn(orderPage);

        AdminOrderListForm result = adminOrderService.getOrderList("ORD", "田中", null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        assertEquals("ORD-001", result.getOrders().get(0).getOrderNumber());
        assertTrue(result.getOrders().get(0).getCustomer().getName().contains("田中"));
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        assertEquals("ORD", result.getSearchOrderNumber());
        assertEquals("田中", result.getSearchCustomerName());

        verify(adminOrderRepository, times(1))
                .findByOrderNumberContainingIgnoreCaseAndCustomerNameContainingIgnoreCaseOrderByIdDesc("ORD", "田中",
                        pageable);
    }

    @Test
    void UT_Service_0066_getOrderList_正常系() {
        // 空文字の検索条件で検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = Arrays.asList(testOrder1, testOrder2, testOrder3);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, 3);

        when(adminOrderRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(orderPage);

        AdminOrderListForm result = adminOrderService.getOrderList("", "", "", pageable);

        assertNotNull(result);
        assertEquals(3, result.getOrders().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertEquals("", result.getSearchOrderNumber());
        assertEquals("", result.getSearchCustomerName());
        assertEquals("", result.getSearchStatus());

        verify(adminOrderRepository, times(1)).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0067_getOrderList_正常系() {
        // 2ページ目の取得
        Pageable pageable = PageRequest.of(1, 5);
        List<Order> orderList = Arrays.asList(testOrder3);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, 3);

        when(adminOrderRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(orderPage);

        AdminOrderListForm result = adminOrderService.getOrderList(null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        assertEquals(2, result.getTotalPages());
        assertEquals(6, result.getTotalElements());
        assertEquals(1, result.getCurrentPage());
        assertFalse(result.isHasNext());
        assertTrue(result.isHasPrevious());

        verify(adminOrderRepository, times(1)).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0068_getOrderList_異常系() {
        // 無効なステータスで検索失敗
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orderList = Arrays.asList(testOrder1, testOrder2, testOrder3);
        Page<Order> orderPage = new PageImpl<>(orderList, pageable, 3);

        when(adminOrderRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(orderPage);

        AdminOrderListForm result = adminOrderService.getOrderList(null, null, "INVALID", pageable);

        assertNotNull(result);
        assertEquals(3, result.getOrders().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertEquals("INVALID", result.getSearchStatus());

        verify(adminOrderRepository, times(1)).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0069_getOrderById_正常系() {
        // 存在する注文IDで詳細取得
        when(adminOrderRepository.findById(1L))
                .thenReturn(Optional.of(testOrder1));

        AdminOrderDto result = adminOrderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ORD-001", result.getOrderNumber());
        assertEquals(Order.OrderStatus.PENDING, result.getStatus());
        assertNotNull(result.getCustomer());
        assertEquals("田中太郎", result.getCustomer().getName());

        verify(adminOrderRepository, times(1)).findById(1L);
    }

    @Test
    void UT_Service_0070_getOrderById_異常系() {
        // 存在しない注文IDで詳細取得失敗
        when(adminOrderRepository.findById(999L))
                .thenReturn(Optional.empty());

        AdminOrderDto result = adminOrderService.getOrderById(999L);

        assertNull(result);

        verify(adminOrderRepository, times(1)).findById(999L);
    }

    @Test
    void UT_Service_0071_getOrderById_異常系() {
        // nullの注文IDで詳細取得失敗
        AdminOrderDto result = adminOrderService.getOrderById(null);

        assertNull(result);

        verify(adminOrderRepository, times(1)).findById(null);
    }

    @Test
    void UT_Service_0072_updateOrderStatus_正常系() {
        // 有効なステータスで更新
        when(adminOrderRepository.findById(1L))
                .thenReturn(Optional.of(testOrder1));
        when(adminOrderRepository.save(any(Order.class)))
                .thenReturn(testOrder1);

        boolean result = adminOrderService.updateOrderStatus(1L, "SHIPPED");

        assertTrue(result);
        assertEquals(Order.OrderStatus.SHIPPED, testOrder1.getStatus());

        verify(adminOrderRepository, times(1)).findById(1L);
        verify(adminOrderRepository, times(1)).save(testOrder1);
    }

    @Test
    void UT_Service_0073_updateOrderStatus_正常系() {
        // 別の有効なステータスで更新
        when(adminOrderRepository.findById(1L))
                .thenReturn(Optional.of(testOrder1));
        when(adminOrderRepository.save(any(Order.class)))
                .thenReturn(testOrder1);

        boolean result = adminOrderService.updateOrderStatus(1L, "PREPARING");

        assertTrue(result);
        assertEquals(Order.OrderStatus.PREPARING, testOrder1.getStatus());

        verify(adminOrderRepository, times(1)).findById(1L);
        verify(adminOrderRepository, times(1)).save(testOrder1);
    }

    @Test
    void UT_Service_0074_updateOrderStatus_異常系() {
        // 存在しない注文IDで更新失敗
        when(adminOrderRepository.findById(999L))
                .thenReturn(Optional.empty());

        boolean result = adminOrderService.updateOrderStatus(999L, "SHIPPED");

        assertFalse(result);

        verify(adminOrderRepository, times(1)).findById(999L);
        verify(adminOrderRepository, never()).save(any(Order.class));
    }

    @Test
    void UT_Service_0075_updateOrderStatus_異常系() {
        // 無効なステータスで更新失敗
        when(adminOrderRepository.findById(1L))
                .thenReturn(Optional.of(testOrder1));

        boolean result = adminOrderService.updateOrderStatus(1L, "INVALID");

        assertFalse(result);

        verify(adminOrderRepository, times(1)).findById(1L);
        verify(adminOrderRepository, never()).save(any(Order.class));
    }

    @Test
    void UT_Service_0076_updateOrderStatus_異常系() {
        // nullの注文IDで更新失敗
        boolean result = adminOrderService.updateOrderStatus(null, "SHIPPED");

        assertFalse(result);

        verify(adminOrderRepository, times(1)).findById(null);
        verify(adminOrderRepository, never()).save(any(Order.class));
    }

    @Test
    void UT_Service_0077_updateOrderStatus_異常系() {
        // nullのステータスで更新失敗
        when(adminOrderRepository.findById(1L))
                .thenReturn(Optional.of(testOrder1));

        boolean result = adminOrderService.updateOrderStatus(1L, null);

        assertFalse(result);

        verify(adminOrderRepository, times(1)).findById(1L);
        verify(adminOrderRepository, never()).save(any(Order.class));
    }

    @Test
    void UT_Service_0078_getAvailableStatuses_正常系() {
        // 利用可能ステータス一覧取得
        List<String> result = adminOrderService.getAvailableStatuses();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("PENDING"));
        assertTrue(result.contains("PREPARING"));
        assertTrue(result.contains("SHIPPED"));
    }
}