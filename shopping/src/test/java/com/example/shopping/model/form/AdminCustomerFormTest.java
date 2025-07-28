package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminCustomerDto;
import com.example.shopping.model.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理者顧客フォームのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminCustomerFormTest {

    @Test
    void UT_Form_0018_fromDto_正常系() {
        // DTOからフォームに変換
        AdminCustomerDto dto = new AdminCustomerDto();
        dto.setId(1L);
        dto.setName("顧客A");
        dto.setEmail("customer@example.com");

        AdminCustomerForm result = AdminCustomerForm.fromDto(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("顧客A", result.getName());
        assertEquals("customer@example.com", result.getEmail());
    }

    @Test
    void UT_Form_0019_toDto_正常系() {
        // フォームからDTOに変換
        AdminCustomerForm form = new AdminCustomerForm();
        form.setId(1L);
        form.setName("顧客A");
        form.setEmail("customer@example.com");

        AdminCustomerDto result = form.toDto();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("顧客A", result.getName());
        assertEquals("customer@example.com", result.getEmail());
    }

    @Test
    void UT_Form_0020_toEntity_正常系() {
        // フォームからエンティティに変換
        AdminCustomerForm form = new AdminCustomerForm();
        form.setId(1L);
        form.setName("顧客A");
        form.setEmail("customer@example.com");

        Customer result = form.toEntity();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("顧客A", result.getName());
        assertEquals("customer@example.com", result.getEmail());
    }

    @Test
    void UT_Form_0021_isNew_正常系() {
        // 新規顧客かどうか判定（idがnullの場合）
        AdminCustomerForm form = new AdminCustomerForm();
        form.setId(null);
        form.setName("顧客A");
        form.setEmail("customer@example.com");

        boolean result = form.isNew();

        assertTrue(result);
    }

    @Test
    void UT_Form_0022_isNew_正常系() {
        // 新規顧客かどうか判定（idが設定されている場合）
        AdminCustomerForm form = new AdminCustomerForm();
        form.setId(1L);
        form.setName("顧客A");
        form.setEmail("customer@example.com");

        boolean result = form.isNew();

        assertFalse(result);
    }
}