package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminProductDto;
import com.example.shopping.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理者商品フォームのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminProductFormTest {

    @Test
    void UT_Form_0004_fromDto_正常系() {
        // DTOからフォームに変換
        AdminProductDto dto = new AdminProductDto();
        dto.setId(1L);
        dto.setName("商品A");
        dto.setPrice(new BigDecimal("1000"));
        dto.setStockQuantity(10);

        AdminProductForm result = AdminProductForm.fromDto(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("商品A", result.getName());
        assertEquals(new BigDecimal("1000"), result.getPrice());
        assertEquals(10, result.getStockQuantity());
    }

    @Test
    void UT_Form_0005_toDto_正常系() {
        // フォームからDTOに変換
        AdminProductForm form = new AdminProductForm();
        form.setId(1L);
        form.setName("商品A");
        form.setPrice(new BigDecimal("1000"));
        form.setStockQuantity(10);

        AdminProductDto result = form.toDto();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("商品A", result.getName());
        assertEquals(new BigDecimal("1000"), result.getPrice());
        assertEquals(10, result.getStockQuantity());
    }

    @Test
    void UT_Form_0006_toEntity_正常系() {
        // フォームからエンティティに変換
        AdminProductForm form = new AdminProductForm();
        form.setId(1L);
        form.setName("商品A");
        form.setPrice(new BigDecimal("1000"));
        form.setStockQuantity(10);

        Product result = form.toEntity();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("商品A", result.getName());
        assertEquals(new BigDecimal("1000"), result.getPrice());
        assertEquals(10, result.getStockQuantity());
    }

    @Test
    void UT_Form_0007_isNew_正常系() {
        // 新規商品かどうか判定（idがnullの場合）
        AdminProductForm form = new AdminProductForm();
        form.setId(null);
        form.setName("商品A");
        form.setPrice(new BigDecimal("1000"));
        form.setStockQuantity(10);

        boolean result = form.isNew();

        assertTrue(result);
    }

    @Test
    void UT_Form_0008_isNew_正常系() {
        // 新規商品かどうか判定（idが設定されている場合）
        AdminProductForm form = new AdminProductForm();
        form.setId(1L);
        form.setName("商品A");
        form.setPrice(new BigDecimal("1000"));
        form.setStockQuantity(10);

        boolean result = form.isNew();

        assertFalse(result);
    }
}