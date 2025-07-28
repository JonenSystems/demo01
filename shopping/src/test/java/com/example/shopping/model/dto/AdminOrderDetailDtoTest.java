package com.example.shopping.model.dto;

import com.example.shopping.model.entity.OrderDetail;
import com.example.shopping.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AdminOrderDetailDtoの単体テスト
 */
class AdminOrderDetailDtoTest {

    private OrderDetail testOrderDetail;
    private AdminOrderDetailDto testOrderDetailDto;
    private Product testProduct;

    @BeforeEach
    void setUp() {
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
        testOrderDetail.setCreatedAt(LocalDateTime.now());
        testOrderDetail.setUpdatedAt(LocalDateTime.now());

        // DTOの準備
        testOrderDetailDto = new AdminOrderDetailDto();
        testOrderDetailDto.setId(1L);
        testOrderDetailDto.setProduct(AdminProductDto.fromEntity(testProduct));
        testOrderDetailDto.setQuantity(2);
        testOrderDetailDto.setUnitPrice(new BigDecimal("1000"));
        testOrderDetailDto.setSubtotal(new BigDecimal("2000"));
        testOrderDetailDto.setCreatedAt(LocalDateTime.now());
        testOrderDetailDto.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * UT_DTO_0017: Entityの値が正しくDTOに変換されることを確認
     */
    @Test
    void UT_DTO_0017_fromEntity_正常系() {
        // 実行
        AdminOrderDetailDto result = AdminOrderDetailDto.fromEntity(testOrderDetail);

        // 検証
        assertEquals(testOrderDetail.getId(), result.getId());
        assertEquals(testOrderDetail.getQuantity(), result.getQuantity());
        assertEquals(testOrderDetail.getUnitPrice(), result.getUnitPrice());
        assertEquals(testOrderDetail.getSubtotal(), result.getSubtotal());
        assertEquals(testOrderDetail.getCreatedAt(), result.getCreatedAt());
        assertEquals(testOrderDetail.getUpdatedAt(), result.getUpdatedAt());

        // 商品情報の検証
        assertNotNull(result.getProduct());
        assertEquals(testOrderDetail.getProduct().getId(), result.getProduct().getId());
        assertEquals(testOrderDetail.getProduct().getName(), result.getProduct().getName());
        assertEquals(testOrderDetail.getProduct().getPrice(), result.getProduct().getPrice());
    }

    /**
     * UT_DTO_0018: DTOの値が正しくEntityに変換されることを確認
     */
    @Test
    void UT_DTO_0018_toEntity_正常系() {
        // 実行
        OrderDetail result = testOrderDetailDto.toEntity();

        // 検証
        assertEquals(testOrderDetailDto.getId(), result.getId());
        assertEquals(testOrderDetailDto.getQuantity(), result.getQuantity());
        assertEquals(testOrderDetailDto.getUnitPrice(), result.getUnitPrice());
        assertEquals(testOrderDetailDto.getSubtotal(), result.getSubtotal());

        // 商品情報の検証
        assertNotNull(result.getProduct());
        assertEquals(testOrderDetailDto.getProduct().getId(), result.getProduct().getId());
        assertEquals(testOrderDetailDto.getProduct().getName(), result.getProduct().getName());
        assertEquals(testOrderDetailDto.getProduct().getPrice(), result.getProduct().getPrice());
    }

    /**
     * fromEntityメソッドでnullが渡された場合のテスト
     */
    @Test
    void fromEntity_null_異常系() {
        // 実行
        AdminOrderDetailDto result = AdminOrderDetailDto.fromEntity(null);

        // 検証
        assertNull(result);
    }
}