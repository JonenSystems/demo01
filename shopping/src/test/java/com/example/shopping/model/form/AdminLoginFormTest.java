package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理者ログインフォームのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminLoginFormTest {

    @Test
    void UT_Form_0001_toDto_正常系() {
        // フォームからDTOに変換
        AdminLoginForm form = new AdminLoginForm();
        form.setUsername("admin");
        form.setPassword("password123");

        AdminUserDto result = form.toDto();

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("password123", result.getPassword());
    }

    @Test
    void UT_Form_0002_fromDto_正常系() {
        // DTOからフォームに変換
        AdminUserDto dto = new AdminUserDto();
        dto.setUsername("admin");
        dto.setPassword("password123");

        AdminLoginForm result = AdminLoginForm.fromDto(dto);

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        // セキュリティのため、パスワードは設定されない
        assertNull(result.getPassword());
    }

    @Test
    void UT_Form_0003_fromDto_異常系() {
        // nullのDTOが渡された場合の処理を確認
        AdminLoginForm result = AdminLoginForm.fromDto(null);

        assertNotNull(result);
        assertNull(result.getUsername());
        assertNull(result.getPassword());
    }
}