package com.example.shopping.service;

import com.example.shopping.model.dto.AdminAuthResultDto;
import com.example.shopping.model.dto.AdminUserDto;
import com.example.shopping.model.entity.User;
import com.example.shopping.repository.AdminUserRepository;
import com.example.shopping.service.impl.AdminAuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 管理者認証サービスのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminAuthServiceTest {

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminAuthServiceImpl adminAuthService;

    private User testAdminUser;
    private User testDisabledUser;
    private User testRegularUser;

    @BeforeEach
    void setUp() {
        // テスト用の管理者ユーザー
        testAdminUser = new User();
        testAdminUser.setId(1L);
        testAdminUser.setUsername("admin");
        testAdminUser.setPassword("$2a$10$JjH0T8QmINM70.g3LC0DAuV/MRJER73OUWFx1dEZi7/DR5Ajs1J9C"); // "password"
        testAdminUser.setRole(User.UserRole.ADMIN);
        testAdminUser.setEnabled(true);
        testAdminUser.setCreatedAt(LocalDateTime.now());
        testAdminUser.setUpdatedAt(LocalDateTime.now());

        // テスト用の無効ユーザー
        testDisabledUser = new User();
        testDisabledUser.setId(2L);
        testDisabledUser.setUsername("disabled");
        testDisabledUser.setPassword("$2a$10$JjH0T8QmINM70.g3LC0DAuV/MRJER73OUWFx1dEZi7/DR5Ajs1J9C");
        testDisabledUser.setRole(User.UserRole.ADMIN);
        testDisabledUser.setEnabled(false);
        testDisabledUser.setCreatedAt(LocalDateTime.now());
        testDisabledUser.setUpdatedAt(LocalDateTime.now());

        // テスト用の一般ユーザー
        testRegularUser = new User();
        testRegularUser.setId(3L);
        testRegularUser.setUsername("user");
        testRegularUser.setPassword("$2a$10$JjH0T8QmINM70.g3LC0DAuV/MRJER73OUWFx1dEZi7/DR5Ajs1J9C");
        testRegularUser.setRole(User.UserRole.USER);
        testRegularUser.setEnabled(true);
        testRegularUser.setCreatedAt(LocalDateTime.now());
        testRegularUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void UT_Service_0001_authenticate_正常系() {
        // 正しいユーザー名とパスワードで認証成功
        when(adminUserRepository.findEnabledAdminByUsername("admin"))
                .thenReturn(Optional.of(testAdminUser));
        when(passwordEncoder.matches("password", testAdminUser.getPassword()))
                .thenReturn(true);
        when(adminUserRepository.findByUsername("admin"))
                .thenReturn(Optional.of(testAdminUser));

        AdminAuthResultDto result = adminAuthService.authenticate("admin", "password");

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.SUCCESS, result.getResultType());
        assertNotNull(result.getUser());
        assertEquals("admin", result.getUser().getUsername());
        assertNull(result.getErrorMessage());
        verify(adminUserRepository).updateLastLogin(1L);
    }

    @Test
    void UT_Service_0002_authenticate_異常系() {
        // 存在しないユーザー名で認証失敗
        when(adminUserRepository.findEnabledAdminByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        AdminAuthResultDto result = adminAuthService.authenticate("nonexistent", "password");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.USER_NOT_FOUND, result.getResultType());
        assertEquals("ユーザーが見つかりません", result.getErrorMessage());
        assertNull(result.getUser());
        verify(adminUserRepository, never()).updateLastLogin(any());
    }

    @Test
    void UT_Service_0003_authenticate_異常系() {
        // 間違ったパスワードで認証失敗
        when(adminUserRepository.findEnabledAdminByUsername("admin"))
                .thenReturn(Optional.of(testAdminUser));
        when(passwordEncoder.matches("wrongpassword", testAdminUser.getPassword()))
                .thenReturn(false);

        AdminAuthResultDto result = adminAuthService.authenticate("admin", "wrongpassword");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.INVALID_PASSWORD, result.getResultType());
        assertEquals("パスワードが正しくありません", result.getErrorMessage());
        assertNull(result.getUser());
        verify(adminUserRepository, never()).updateLastLogin(any());
    }

    @Test
    void UT_Service_0004_authenticate_異常系() {
        // nullのユーザー名で認証失敗
        AdminAuthResultDto result = adminAuthService.authenticate(null, "password");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.FAILURE, result.getResultType());
        assertEquals("ユーザー名を入力してください", result.getErrorMessage());
        assertNull(result.getUser());
        verify(adminUserRepository, never()).findEnabledAdminByUsername(any());
    }

    @Test
    void UT_Service_0005_authenticate_異常系() {
        // 空文字のユーザー名で認証失敗
        AdminAuthResultDto result = adminAuthService.authenticate("", "password");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.FAILURE, result.getResultType());
        assertEquals("ユーザー名を入力してください", result.getErrorMessage());
        assertNull(result.getUser());
        verify(adminUserRepository, never()).findEnabledAdminByUsername(any());
    }

    @Test
    void UT_Service_0006_authenticate_異常系() {
        // nullのパスワードで認証失敗
        AdminAuthResultDto result = adminAuthService.authenticate("admin", null);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.FAILURE, result.getResultType());
        assertEquals("パスワードを入力してください", result.getErrorMessage());
        assertNull(result.getUser());
        verify(adminUserRepository, never()).findEnabledAdminByUsername(any());
    }

    @Test
    void UT_Service_0007_authenticate_異常系() {
        // 空文字のパスワードで認証失敗
        AdminAuthResultDto result = adminAuthService.authenticate("admin", "");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.FAILURE, result.getResultType());
        assertEquals("パスワードを入力してください", result.getErrorMessage());
        assertNull(result.getUser());
        verify(adminUserRepository, never()).findEnabledAdminByUsername(any());
    }

    @Test
    void UT_Service_0008_updateLastLogin_正常系() {
        // 有効なユーザーIDで最終ログイン時刻更新
        when(adminUserRepository.updateLastLogin(1L))
                .thenReturn(Optional.of(testAdminUser));

        AdminUserDto result = adminAuthService.updateLastLogin(1L);

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals(1L, result.getId());
        verify(adminUserRepository).updateLastLogin(1L);
    }

    @Test
    void UT_Service_0009_updateLastLogin_異常系() {
        // 存在しないユーザーIDで更新失敗
        when(adminUserRepository.updateLastLogin(999L))
                .thenReturn(Optional.empty());

        AdminUserDto result = adminAuthService.updateLastLogin(999L);

        assertNull(result);
        verify(adminUserRepository).updateLastLogin(999L);
    }

    @Test
    void UT_Service_0010_updateLastLogin_異常系() {
        // nullのユーザーIDで更新失敗
        when(adminUserRepository.updateLastLogin(null))
                .thenReturn(Optional.empty());

        AdminUserDto result = adminAuthService.updateLastLogin(null);

        assertNull(result);
        verify(adminUserRepository).updateLastLogin(null);
    }

    @Test
    void UT_Service_0011_findAdminByUsername_正常系() {
        // 存在するユーザー名で検索成功
        when(adminUserRepository.findAdminByUsername("admin"))
                .thenReturn(Optional.of(testAdminUser));

        AdminUserDto result = adminAuthService.findAdminByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertTrue(result.isAdmin());
        assertEquals(User.UserRole.ADMIN, result.getRole());
        verify(adminUserRepository).findAdminByUsername("admin");
    }

    @Test
    void UT_Service_0012_findAdminByUsername_異常系() {
        // 存在しないユーザー名で検索失敗
        when(adminUserRepository.findAdminByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        AdminUserDto result = adminAuthService.findAdminByUsername("nonexistent");

        assertNull(result);
        verify(adminUserRepository).findAdminByUsername("nonexistent");
    }

    @Test
    void UT_Service_0013_findAdminByUsername_異常系() {
        // nullのユーザー名で検索失敗
        when(adminUserRepository.findAdminByUsername(null))
                .thenReturn(Optional.empty());

        AdminUserDto result = adminAuthService.findAdminByUsername(null);

        assertNull(result);
        verify(adminUserRepository).findAdminByUsername(null);
    }

    @Test
    void UT_Service_0014_findAdminByUsername_異常系() {
        // 空文字のユーザー名で検索失敗
        when(adminUserRepository.findAdminByUsername(""))
                .thenReturn(Optional.empty());

        AdminUserDto result = adminAuthService.findAdminByUsername("");

        assertNull(result);
        verify(adminUserRepository).findAdminByUsername("");
    }

    @Test
    void UT_Service_0015_existsAdminByUsername_正常系() {
        // 存在するユーザー名で確認
        when(adminUserRepository.existsAdminByUsername("admin"))
                .thenReturn(true);

        boolean result = adminAuthService.existsAdminByUsername("admin");

        assertTrue(result);
        verify(adminUserRepository).existsAdminByUsername("admin");
    }

    @Test
    void UT_Service_0016_existsAdminByUsername_正常系() {
        // 存在しないユーザー名で確認
        when(adminUserRepository.existsAdminByUsername("nonexistent"))
                .thenReturn(false);

        boolean result = adminAuthService.existsAdminByUsername("nonexistent");

        assertFalse(result);
        verify(adminUserRepository).existsAdminByUsername("nonexistent");
    }

    @Test
    void UT_Service_0017_existsAdminByUsername_異常系() {
        // nullのユーザー名で確認
        when(adminUserRepository.existsAdminByUsername(null))
                .thenReturn(false);

        boolean result = adminAuthService.existsAdminByUsername(null);

        assertFalse(result);
        verify(adminUserRepository).existsAdminByUsername(null);
    }
}