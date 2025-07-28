package com.example.shopping.model.dto;

import com.example.shopping.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AdminAuthResultDtoの単体テスト
 */
class AdminAuthResultDtoTest {

    private AdminUserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUserDto = new AdminUserDto();
        testUserDto.setId(1L);
        testUserDto.setUsername("admin");
        testUserDto.setRole(User.UserRole.ADMIN);
        testUserDto.setEnabled(true);
    }

    /**
     * UT_DTO_0001: 認証成功時の結果が正しく作成されることを確認
     */
    @Test
    void UT_DTO_0001_success_正常系() {
        // 実行
        AdminAuthResultDto result = AdminAuthResultDto.success(testUserDto);

        // 検証
        assertTrue(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.SUCCESS, result.getResultType());
        assertEquals(testUserDto, result.getUser());
        assertNull(result.getErrorMessage());
    }

    /**
     * UT_DTO_0002: 認証失敗時の結果が正しく作成されることを確認
     */
    @Test
    void UT_DTO_0002_failure_正常系() {
        // 実行
        AdminAuthResultDto result = AdminAuthResultDto.failure(
                AdminAuthResultDto.AuthResultType.INVALID_PASSWORD,
                "ユーザー名またはパスワードが正しくありません");

        // 検証
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.INVALID_PASSWORD, result.getResultType());
        assertEquals("ユーザー名またはパスワードが正しくありません", result.getErrorMessage());
        assertNull(result.getUser());
    }

    /**
     * UT_DTO_0003: デフォルトメッセージで認証失敗時の結果が作成されることを確認
     */
    @Test
    void UT_DTO_0003_failure_正常系() {
        // 実行
        AdminAuthResultDto result = AdminAuthResultDto.failure(
                AdminAuthResultDto.AuthResultType.USER_DISABLED);

        // 検証
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.USER_DISABLED, result.getResultType());
        assertEquals("ユーザーが無効です", result.getErrorMessage());
        assertNull(result.getUser());
    }
}