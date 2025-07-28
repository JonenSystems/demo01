package com.example.shopping.model.dto;

import com.example.shopping.model.entity.Order;
import com.example.shopping.model.entity.Customer;
import com.example.shopping.model.entity.Product;
import com.example.shopping.model.entity.OrderDetail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminOrderDtoの単体テスト
 */
class AdminOrderDtoTest {

    private Order testOrder;
    private AdminOrderDto testOrderDto;
    private Customer testCustomer;
    private Product testProduct;
    private OrderDetail testOrderDetail;

    @BeforeEach
    void setUp() {
        // 顧客の準備
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("顧客A");
        testCustomer.setEmail("customer@example.com");

        // 商品の準備
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("商品A");
        testProduct.setPrice(new BigDecimal("1000"));

        // 注文詳細の準備
        testOrderDetail = new OrderDetail();
        testOrderDetail.setId(1L);
        testOrderDetail.setProduct(testProduct);
        testOrderDetail.setQuantity(2);
        testOrderDetail.setUnitPrice(new BigDecimal("1000"));
        testOrderDetail.setSubtotal(new BigDecimal("2000"));

        // 注文の準備
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-001");
        testOrder.setCustomer(testCustomer);
        testOrder.setTotalAmount(new BigDecimal("3000"));
        testOrder.setStatus(Order.OrderStatus.PENDING);
        testOrder.setCreatedAt(LocalDateTime.now());
        testOrder.setUpdatedAt(LocalDateTime.now());

        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(testOrderDetail);
        testOrder.setOrderDetails(orderDetails);

        // DTOの準備
        testOrderDto = new AdminOrderDto();
        testOrderDto.setId(1L);
        testOrderDto.setOrderNumber("ORD-001");
        testOrderDto.setCustomer(AdminCustomerDto.fromEntity(testCustomer));
        testOrderDto.setTotalAmount(new BigDecimal("3000"));
        testOrderDto.setStatus(Order.OrderStatus.PENDING);
        testOrderDto.setCreatedAt(LocalDateTime.now());
        testOrderDto.setUpdatedAt(LocalDateTime.now());

        List<AdminOrderDetailDto> orderDetailDtos = new ArrayList<>();
        orderDetailDtos.add(AdminOrderDetailDto.fromEntity(testOrderDetail));
        testOrderDto.setOrderDetails(orderDetailDtos);
    }

    /**
     * UT_DTO_0015: Entityの値が正しくDTOに変換されることを確認
     */
    @Test
    void UT_DTO_0015_fromEntity_正常系() {
        // 実行
        AdminOrderDto result = AdminOrderDto.fromEntity(testOrder);

        // 検証
        assertEquals(testOrder.getId(), result.getId());
        assertEquals(testOrder.getOrderNumber(), result.getOrderNumber());
        assertEquals(testOrder.getTotalAmount(), result.getTotalAmount());
        assertEquals(testOrder.getStatus(), result.getStatus());
        assertEquals(testOrder.getCreatedAt(), result.getCreatedAt());
        assertEquals(testOrder.getUpdatedAt(), result.getUpdatedAt());

        // 顧客情報の検証
        assertNotNull(result.getCustomer());
        assertEquals(testOrder.getCustomer().getId(), result.getCustomer().getId());
        assertEquals(testOrder.getCustomer().getName(), result.getCustomer().getName());
        assertEquals(testOrder.getCustomer().getEmail(), result.getCustomer().getEmail());

        // 注文詳細の検証
        assertNotNull(result.getOrderDetails());
        assertEquals(1, result.getOrderDetails().size());
        assertEquals(testOrder.getOrderDetails().get(0).getId(), result.getOrderDetails().get(0).getId());
    }

    /**
     * UT_DTO_0016: DTOの値が正しくEntityに変換されることを確認
     */
    @Test
    void UT_DTO_0016_toEntity_正常系() {
        // 実行
        Order result = testOrderDto.toEntity();

        // 検証
        assertEquals(testOrderDto.getId(), result.getId());
        assertEquals(testOrderDto.getOrderNumber(), result.getOrderNumber());
        assertEquals(testOrderDto.getTotalAmount(), result.getTotalAmount());
        assertEquals(testOrderDto.getStatus(), result.getStatus());

        // 顧客情報の検証
        assertNotNull(result.getCustomer());
        assertEquals(testOrderDto.getCustomer().getId(), result.getCustomer().getId());
        assertEquals(testOrderDto.getCustomer().getName(), result.getCustomer().getName());
        assertEquals(testOrderDto.getCustomer().getEmail(), result.getCustomer().getEmail());
    }

    /**
     * fromEntityメソッドでnullが渡された場合のテスト
     */
    @Test
    void fromEntity_null_異常系() {
        // 実行
        AdminOrderDto result = AdminOrderDto.fromEntity(null);

        // 検証
        assertNull(result);
    }
}