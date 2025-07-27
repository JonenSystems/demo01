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
    void authenticate_WithValidCredentials_ShouldReturnSuccess() {
        // 有効な認証情報でのテスト
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
        verify(adminUserRepository).updateLastLogin(1L);
    }

    @Test
    void authenticate_WithInvalidUsername_ShouldReturnUserNotFound() {
        // 存在しないユーザー名でのテスト
        when(adminUserRepository.findEnabledAdminByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        AdminAuthResultDto result = adminAuthService.authenticate("nonexistent", "password");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.USER_NOT_FOUND, result.getResultType());
        verify(adminUserRepository, never()).updateLastLogin(any());
    }

    @Test
    void authenticate_WithInvalidPassword_ShouldReturnInvalidPassword() {
        // 間違ったパスワードでのテスト
        when(adminUserRepository.findEnabledAdminByUsername("admin"))
                .thenReturn(Optional.of(testAdminUser));
        when(passwordEncoder.matches("wrongpassword", testAdminUser.getPassword()))
                .thenReturn(false);

        AdminAuthResultDto result = adminAuthService.authenticate("admin", "wrongpassword");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.INVALID_PASSWORD, result.getResultType());
        verify(adminUserRepository, never()).updateLastLogin(any());
    }

    @Test
    void authenticate_WithDisabledUser_ShouldReturnUserDisabled() {
        // 無効なユーザーでのテスト
        when(adminUserRepository.findEnabledAdminByUsername("disabled"))
                .thenReturn(Optional.of(testDisabledUser));

        AdminAuthResultDto result = adminAuthService.authenticate("disabled", "password");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.USER_DISABLED, result.getResultType());
        verify(adminUserRepository, never()).updateLastLogin(any());
    }

    @Test
    void authenticate_WithNullUsername_ShouldReturnFailure() {
        // nullユーザー名でのテスト
        AdminAuthResultDto result = adminAuthService.authenticate(null, "password");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.FAILURE, result.getResultType());
        assertEquals("ユーザー名を入力してください", result.getErrorMessage());
        verify(adminUserRepository, never()).findEnabledAdminByUsername(any());
    }

    @Test
    void authenticate_WithEmptyUsername_ShouldReturnFailure() {
        // 空のユーザー名でのテスト
        AdminAuthResultDto result = adminAuthService.authenticate("", "password");

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.FAILURE, result.getResultType());
        assertEquals("ユーザー名を入力してください", result.getErrorMessage());
        verify(adminUserRepository, never()).findEnabledAdminByUsername(any());
    }

    @Test
    void authenticate_WithNullPassword_ShouldReturnFailure() {
        // nullパスワードでのテスト
        AdminAuthResultDto result = adminAuthService.authenticate("admin", null);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(AdminAuthResultDto.AuthResultType.FAILURE, result.getResultType());
        assertEquals("パスワードを入力してください", result.getErrorMessage());
        verify(adminUserRepository, never()).findEnabledAdminByUsername(any());
    }

    @Test
    void updateLastLogin_WithValidUserId_ShouldReturnUpdatedUser() {
        // 有効なユーザーIDでの最終ログイン時刻更新テスト
        when(adminUserRepository.updateLastLogin(1L))
                .thenReturn(Optional.of(testAdminUser));

        AdminUserDto result = adminAuthService.updateLastLogin(1L);

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        verify(adminUserRepository).updateLastLogin(1L);
    }

    @Test
    void updateLastLogin_WithInvalidUserId_ShouldReturnNull() {
        // 無効なユーザーIDでの最終ログイン時刻更新テスト
        when(adminUserRepository.updateLastLogin(999L))
                .thenReturn(Optional.empty());

        AdminUserDto result = adminAuthService.updateLastLogin(999L);

        assertNull(result);
        verify(adminUserRepository).updateLastLogin(999L);
    }

    @Test
    void findAdminByUsername_WithValidUsername_ShouldReturnUser() {
        // 有効なユーザー名での管理者ユーザー検索テスト
        when(adminUserRepository.findAdminByUsername("admin"))
                .thenReturn(Optional.of(testAdminUser));

        AdminUserDto result = adminAuthService.findAdminByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertTrue(result.isAdmin());
        verify(adminUserRepository).findAdminByUsername("admin");
    }

    @Test
    void findAdminByUsername_WithInvalidUsername_ShouldReturnNull() {
        // 無効なユーザー名での管理者ユーザー検索テスト
        when(adminUserRepository.findAdminByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        AdminUserDto result = adminAuthService.findAdminByUsername("nonexistent");

        assertNull(result);
        verify(adminUserRepository).findAdminByUsername("nonexistent");
    }

    @Test
    void existsAdminByUsername_WithExistingAdmin_ShouldReturnTrue() {
        // 存在する管理者ユーザーの確認テスト
        when(adminUserRepository.existsAdminByUsername("admin"))
                .thenReturn(true);

        boolean result = adminAuthService.existsAdminByUsername("admin");

        assertTrue(result);
        verify(adminUserRepository).existsAdminByUsername("admin");
    }

    @Test
    void existsAdminByUsername_WithNonExistingAdmin_ShouldReturnFalse() {
        // 存在しない管理者ユーザーの確認テスト
        when(adminUserRepository.existsAdminByUsername("nonexistent"))
                .thenReturn(false);

        boolean result = adminAuthService.existsAdminByUsername("nonexistent");

        assertFalse(result);
        verify(adminUserRepository).existsAdminByUsername("nonexistent");
    }
}