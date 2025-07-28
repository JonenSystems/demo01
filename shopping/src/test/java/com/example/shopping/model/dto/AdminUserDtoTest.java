package com.example.shopping.model.dto;

import com.example.shopping.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * AdminUserDtoの単体テスト
 */
class AdminUserDtoTest {

    private User testUser;
    private AdminUserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("admin");
        testUser.setPassword("hashedPassword");
        testUser.setEnabled(true);
        testUser.setRole(User.UserRole.ADMIN);
        testUser.setLastLogin(LocalDateTime.now());
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        testUserDto = new AdminUserDto();
        testUserDto.setId(1L);
        testUserDto.setUsername("admin");
        testUserDto.setPassword("hashedPassword");
        testUserDto.setEnabled(true);
        testUserDto.setRole(User.UserRole.ADMIN);
        testUserDto.setLastLogin(LocalDateTime.now());
        testUserDto.setCreatedAt(LocalDateTime.now());
        testUserDto.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * UT_DTO_0004: Entityの値が正しくDTOに変換されることを確認
     */
    @Test
    void UT_DTO_0004_fromEntity_正常系() {
        // 実行
        AdminUserDto result = AdminUserDto.fromEntity(testUser);

        // 検証
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getRole(), result.getRole());
        assertEquals(testUser.getEnabled(), result.getEnabled());
        assertEquals(testUser.getLastLogin(), result.getLastLogin());
        assertEquals(testUser.getCreatedAt(), result.getCreatedAt());
        assertEquals(testUser.getUpdatedAt(), result.getUpdatedAt());
        // セキュリティのため、パスワードは設定されない
        assertNull(result.getPassword());
    }

    /**
     * UT_DTO_0005: DTOの値が正しくEntityに変換されることを確認
     */
    @Test
    void UT_DTO_0005_toEntity_正常系() {
        // 実行
        User result = testUserDto.toEntity();

        // 検証
        assertEquals(testUserDto.getId(), result.getId());
        assertEquals(testUserDto.getUsername(), result.getUsername());
        assertEquals(testUserDto.getPassword(), result.getPassword());
        assertEquals(testUserDto.getRole(), result.getRole());
        assertEquals(testUserDto.getEnabled(), result.getEnabled());
        assertEquals(testUserDto.getLastLogin(), result.getLastLogin());
        assertEquals(testUserDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(testUserDto.getUpdatedAt(), result.getUpdatedAt());
    }

    /**
     * UT_DTO_0006: ADMINロールの場合にtrueが返されることを確認
     */
    @Test
    void UT_DTO_0006_isAdmin_正常系() {
        // 準備
        testUserDto.setRole(User.UserRole.ADMIN);

        // 実行
        boolean result = testUserDto.isAdmin();

        // 検証
        assertTrue(result);
    }

    /**
     * UT_DTO_0007: USERロールの場合にfalseが返されることを確認
     */
    @Test
    void UT_DTO_0007_isAdmin_正常系() {
        // 準備
        testUserDto.setRole(User.UserRole.USER);

        // 実行
        boolean result = testUserDto.isAdmin();

        // 検証
        assertFalse(result);
    }

    /**
     * UT_DTO_0008: roleがnullの場合にfalseが返されることを確認
     */
    @Test
    void UT_DTO_0008_isAdmin_異常系() {
        // 準備
        testUserDto.setRole(null);

        // 実行
        boolean result = testUserDto.isAdmin();

        // 検証
        assertFalse(result);
    }

    /**
     * UT_DTO_0009: enabledがtrueの場合にtrueが返されることを確認
     */
    @Test
    void UT_DTO_0009_isEnabled_正常系() {
        // 準備
        testUserDto.setEnabled(true);

        // 実行
        boolean result = testUserDto.isEnabled();

        // 検証
        assertTrue(result);
    }

    /**
     * UT_DTO_0010: enabledがfalseの場合にfalseが返されることを確認
     */
    @Test
    void UT_DTO_0010_isEnabled_正常系() {
        // 準備
        testUserDto.setEnabled(false);

        // 実行
        boolean result = testUserDto.isEnabled();

        // 検証
        assertFalse(result);
    }
}