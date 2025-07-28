package com.example.shopping.model.dto;

import com.example.shopping.model.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * AdminCustomerDtoの単体テスト
 */
class AdminCustomerDtoTest {

    private Customer testCustomer;
    private AdminCustomerDto testCustomerDto;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("顧客A");
        testCustomer.setEmail("customer@example.com");
        testCustomer.setPhone("090-1234-5678");
        testCustomer.setAddress("東京都渋谷区1-1-1");
        testCustomer.setCreatedAt(LocalDateTime.now());
        testCustomer.setUpdatedAt(LocalDateTime.now());

        testCustomerDto = new AdminCustomerDto();
        testCustomerDto.setId(1L);
        testCustomerDto.setName("顧客A");
        testCustomerDto.setEmail("customer@example.com");
        testCustomerDto.setPhone("090-1234-5678");
        testCustomerDto.setAddress("東京都渋谷区1-1-1");
        testCustomerDto.setCreatedAt(LocalDateTime.now());
        testCustomerDto.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * UT_DTO_0013: Entityの値が正しくDTOに変換されることを確認
     */
    @Test
    void UT_DTO_0013_fromEntity_正常系() {
        // 実行
        AdminCustomerDto result = AdminCustomerDto.fromEntity(testCustomer);

        // 検証
        assertEquals(testCustomer.getId(), result.getId());
        assertEquals(testCustomer.getName(), result.getName());
        assertEquals(testCustomer.getEmail(), result.getEmail());
        assertEquals(testCustomer.getPhone(), result.getPhone());
        assertEquals(testCustomer.getAddress(), result.getAddress());
        assertEquals(testCustomer.getCreatedAt(), result.getCreatedAt());
        assertEquals(testCustomer.getUpdatedAt(), result.getUpdatedAt());
    }

    /**
     * UT_DTO_0014: DTOの値が正しくEntityに変換されることを確認
     */
    @Test
    void UT_DTO_0014_toEntity_正常系() {
        // 実行
        Customer result = testCustomerDto.toEntity();

        // 検証
        assertEquals(testCustomerDto.getId(), result.getId());
        assertEquals(testCustomerDto.getName(), result.getName());
        assertEquals(testCustomerDto.getEmail(), result.getEmail());
        assertEquals(testCustomerDto.getPhone(), result.getPhone());
        assertEquals(testCustomerDto.getAddress(), result.getAddress());
    }

    /**
     * fromEntityメソッドでnullが渡された場合のテスト
     */
    @Test
    void fromEntity_null_異常系() {
        // 実行
        AdminCustomerDto result = AdminCustomerDto.fromEntity(null);

        // 検証
        assertNull(result);
    }
}