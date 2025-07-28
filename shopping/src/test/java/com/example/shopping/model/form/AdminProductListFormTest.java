package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理者商品一覧フォームのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminProductListFormTest {

    @Test
    void UT_Form_0009_fromDtoList_正常系() {
        // DTOリストからフォームに変換
        AdminProductDto product1 = new AdminProductDto();
        product1.setId(1L);
        product1.setName("商品A");
        product1.setPrice(new BigDecimal("1000"));

        AdminProductDto product2 = new AdminProductDto();
        product2.setId(2L);
        product2.setName("商品B");
        product2.setPrice(new BigDecimal("2000"));

        List<AdminProductDto> products = Arrays.asList(product1, product2);

        AdminProductListForm result = AdminProductListForm.fromDtoList(products);

        assertNotNull(result);
        assertNotNull(result.getProducts());
        assertEquals(2, result.getProducts().size());
        assertEquals("商品A", result.getProducts().get(0).getName());
        assertEquals("商品B", result.getProducts().get(1).getName());
    }

    @Test
    void UT_Form_0010_fromDtoList_異常系() {
        // 空のリストが渡された場合の処理を確認
        List<AdminProductDto> products = Collections.emptyList();

        AdminProductListForm result = AdminProductListForm.fromDtoList(products);

        assertNotNull(result);
        assertNotNull(result.getProducts());
        assertEquals(0, result.getProducts().size());
    }

    @Test
    void UT_Form_0011_fromDtoList_異常系() {
        // nullのリストが渡された場合の処理を確認
        AdminProductListForm result = AdminProductListForm.fromDtoList(null);

        assertNotNull(result);
        assertNull(result.getProducts());
    }

    @Test
    void UT_Form_0012_hasProducts_正常系() {
        // 商品が存在するかチェック（商品リストが存在する場合）
        AdminProductDto product = new AdminProductDto();
        product.setId(1L);
        product.setName("商品A");

        AdminProductListForm form = new AdminProductListForm();
        form.setProducts(Arrays.asList(product));

        boolean result = form.hasProducts();

        assertTrue(result);
    }

    @Test
    void UT_Form_0013_hasProducts_正常系() {
        // 商品が存在するかチェック（商品リストが空の場合）
        AdminProductListForm form = new AdminProductListForm();
        form.setProducts(Collections.emptyList());

        boolean result = form.hasProducts();

        assertFalse(result);
    }

    @Test
    void UT_Form_0014_hasProducts_異常系() {
        // 商品が存在するかチェック（商品リストがnullの場合）
        AdminProductListForm form = new AdminProductListForm();
        form.setProducts(null);

        boolean result = form.hasProducts();

        assertFalse(result);
    }

    @Test
    void UT_Form_0015_getProductCount_正常系() {
        // 商品数を取得（商品リストの要素数が正しく返されることを確認）
        AdminProductDto product1 = new AdminProductDto();
        product1.setId(1L);
        product1.setName("商品A");

        AdminProductDto product2 = new AdminProductDto();
        product2.setId(2L);
        product2.setName("商品B");

        AdminProductDto product3 = new AdminProductDto();
        product3.setId(3L);
        product3.setName("商品C");

        AdminProductListForm form = new AdminProductListForm();
        form.setProducts(Arrays.asList(product1, product2, product3));

        int result = form.getProductCount();

        assertEquals(3, result);
    }

    @Test
    void UT_Form_0016_getProductCount_正常系() {
        // 商品数を取得（商品リストが空の場合に0が返されることを確認）
        AdminProductListForm form = new AdminProductListForm();
        form.setProducts(Collections.emptyList());

        int result = form.getProductCount();

        assertEquals(0, result);
    }

    @Test
    void UT_Form_0017_getProductCount_異常系() {
        // 商品数を取得（商品リストがnullの場合に0が返されることを確認）
        AdminProductListForm form = new AdminProductListForm();
        form.setProducts(null);

        int result = form.getProductCount();

        assertEquals(0, result);
    }
}