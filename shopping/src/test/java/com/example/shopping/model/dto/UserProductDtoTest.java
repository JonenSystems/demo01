package com.example.shopping.model.dto;

import com.example.shopping.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * UserProductDtoの単体テスト
 */
class UserProductDtoTest {

    private Product testProduct;
    private UserProductDto testProductDto;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("商品A");
        testProduct.setDescription("商品Aの説明");
        testProduct.setPrice(new BigDecimal("1000"));
        testProduct.setCategory("カテゴリA");
        testProduct.setStockQuantity(10);
        testProduct.setImagePath("/images/product-a.jpg");

        testProductDto = new UserProductDto();
        testProductDto.setId(1L);
        testProductDto.setName("商品A");
        testProductDto.setDescription("商品Aの説明");
        testProductDto.setPrice(new BigDecimal("1000"));
        testProductDto.setCategory("カテゴリA");
        testProductDto.setStockQuantity(10);
        testProductDto.setImagePath("/images/product-a.jpg");
    }

    /**
     * UT_DTO_0019: Entityの値が正しくDTOに変換されることを確認
     */
    @Test
    void UT_DTO_0019_fromEntity_正常系() {
        // 実行
        UserProductDto result = UserProductDto.fromEntity(testProduct);

        // 検証
        assertEquals(testProduct.getId(), result.getId());
        assertEquals(testProduct.getName(), result.getName());
        assertEquals(testProduct.getDescription(), result.getDescription());
        assertEquals(testProduct.getPrice(), result.getPrice());
        assertEquals(testProduct.getCategory(), result.getCategory());
        assertEquals(testProduct.getStockQuantity(), result.getStockQuantity());
        assertEquals(testProduct.getImagePath(), result.getImagePath());
    }

    /**
     * UT_DTO_0020: DTOの値が正しくEntityに変換されることを確認
     */
    @Test
    void UT_DTO_0020_toEntity_正常系() {
        // 実行
        Product result = testProductDto.toEntity();

        // 検証
        assertEquals(testProductDto.getId(), result.getId());
        assertEquals(testProductDto.getName(), result.getName());
        assertEquals(testProductDto.getDescription(), result.getDescription());
        assertEquals(testProductDto.getPrice(), result.getPrice());
        assertEquals(testProductDto.getCategory(), result.getCategory());
        assertEquals(testProductDto.getStockQuantity(), result.getStockQuantity());
        assertEquals(testProductDto.getImagePath(), result.getImagePath());
    }

    /**
     * fromEntityメソッドでnullが渡された場合のテスト
     */
    @Test
    void fromEntity_null_異常系() {
        // 実行
        UserProductDto result = UserProductDto.fromEntity(null);

        // 検証
        assertNull(result);
    }
}