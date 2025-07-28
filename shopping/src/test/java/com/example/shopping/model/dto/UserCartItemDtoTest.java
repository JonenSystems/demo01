package com.example.shopping.model.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * UserCartItemDtoの単体テスト
 */
class UserCartItemDtoTest {

    private UserCartItemDto testCartItemDto;

    @BeforeEach
    void setUp() {
        testCartItemDto = new UserCartItemDto();
        testCartItemDto.setProductId(1L);
        testCartItemDto.setProductName("商品A");
        testCartItemDto.setProductImagePath("/images/product-a.jpg");
        testCartItemDto.setUnitPrice(new BigDecimal("1000"));
        testCartItemDto.setQuantity(3);
    }

    /**
     * UT_DTO_0021: 数量と価格から小計が正しく計算されることを確認
     */
    @Test
    void UT_DTO_0021_calculateSubtotal_正常系() {
        // 実行
        testCartItemDto.calculateSubtotal();

        // 検証
        assertEquals(new BigDecimal("3000"), testCartItemDto.getSubtotal());
    }

    /**
     * calculateSubtotalメソッドでunitPriceがnullの場合のテスト
     */
    @Test
    void calculateSubtotal_unitPrice_null_異常系() {
        // 準備
        testCartItemDto.setUnitPrice(null);
        testCartItemDto.setQuantity(3);

        // 実行
        testCartItemDto.calculateSubtotal();

        // 検証
        assertNull(testCartItemDto.getSubtotal());
    }

    /**
     * calculateSubtotalメソッドでquantityがnullの場合のテスト
     */
    @Test
    void calculateSubtotal_quantity_null_異常系() {
        // 準備
        testCartItemDto.setUnitPrice(new BigDecimal("1000"));
        testCartItemDto.setQuantity(null);

        // 実行
        testCartItemDto.calculateSubtotal();

        // 検証
        assertNull(testCartItemDto.getSubtotal());
    }

    /**
     * calculateSubtotalメソッドでunitPriceとquantityが両方nullの場合のテスト
     */
    @Test
    void calculateSubtotal_both_null_異常系() {
        // 準備
        testCartItemDto.setUnitPrice(null);
        testCartItemDto.setQuantity(null);

        // 実行
        testCartItemDto.calculateSubtotal();

        // 検証
        assertNull(testCartItemDto.getSubtotal());
    }

    /**
     * calculateSubtotalメソッドでquantityが0の場合のテスト
     */
    @Test
    void calculateSubtotal_quantity_zero_正常系() {
        // 準備
        testCartItemDto.setUnitPrice(new BigDecimal("1000"));
        testCartItemDto.setQuantity(0);

        // 実行
        testCartItemDto.calculateSubtotal();

        // 検証
        assertEquals(BigDecimal.ZERO, testCartItemDto.getSubtotal());
    }
}