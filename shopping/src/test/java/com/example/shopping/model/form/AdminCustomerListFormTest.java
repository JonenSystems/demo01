package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminCustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理者顧客一覧フォームのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminCustomerListFormTest {

    @Test
    void UT_Form_0023_fromDtoList_正常系() {
        // DTOリストからフォームに変換
        AdminCustomerDto customer1 = new AdminCustomerDto();
        customer1.setId(1L);
        customer1.setName("顧客A");
        customer1.setEmail("customer1@example.com");

        AdminCustomerDto customer2 = new AdminCustomerDto();
        customer2.setId(2L);
        customer2.setName("顧客B");
        customer2.setEmail("customer2@example.com");

        List<AdminCustomerDto> customers = Arrays.asList(customer1, customer2);

        AdminCustomerListForm result = AdminCustomerListForm.fromDtoList(customers);

        assertNotNull(result);
        assertNotNull(result.getCustomers());
        assertEquals(2, result.getCustomers().size());
        assertEquals("顧客A", result.getCustomers().get(0).getName());
        assertEquals("顧客B", result.getCustomers().get(1).getName());
    }

    @Test
    void UT_Form_0024_fromDtoList_異常系() {
        // 空のリストが渡された場合の処理を確認
        List<AdminCustomerDto> customers = Collections.emptyList();

        AdminCustomerListForm result = AdminCustomerListForm.fromDtoList(customers);

        assertNotNull(result);
        assertNotNull(result.getCustomers());
        assertEquals(0, result.getCustomers().size());
    }

    @Test
    void UT_Form_0025_fromDtoList_異常系() {
        // nullのリストが渡された場合の処理を確認
        AdminCustomerListForm result = AdminCustomerListForm.fromDtoList(null);

        assertNotNull(result);
        assertNull(result.getCustomers());
    }

    @Test
    void UT_Form_0026_hasCustomers_正常系() {
        // 顧客が存在するかチェック（顧客リストが存在する場合）
        AdminCustomerDto customer = new AdminCustomerDto();
        customer.setId(1L);
        customer.setName("顧客A");
        customer.setEmail("customer@example.com");

        AdminCustomerListForm form = new AdminCustomerListForm();
        form.setCustomers(Arrays.asList(customer));

        boolean result = form.hasCustomers();

        assertTrue(result);
    }

    @Test
    void UT_Form_0027_hasCustomers_正常系() {
        // 顧客が存在するかチェック（顧客リストが空の場合）
        AdminCustomerListForm form = new AdminCustomerListForm();
        form.setCustomers(Collections.emptyList());

        boolean result = form.hasCustomers();

        assertFalse(result);
    }

    @Test
    void UT_Form_0028_hasCustomers_異常系() {
        // 顧客が存在するかチェック（顧客リストがnullの場合）
        AdminCustomerListForm form = new AdminCustomerListForm();
        form.setCustomers(null);

        boolean result = form.hasCustomers();

        assertFalse(result);
    }
}