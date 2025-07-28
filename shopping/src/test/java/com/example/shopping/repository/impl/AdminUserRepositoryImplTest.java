package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AdminUserRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class AdminUserRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<User> userQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    @InjectMocks
    private AdminUserRepositoryImpl adminUserRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("admin");
        testUser.setPassword("hashedPassword");
        testUser.setRole(User.UserRole.ADMIN);
        testUser.setEnabled(true);
    }

    @Test
    void UT_Repository_0001_findByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenReturn(testUser);

        // テスト実行
        Optional<User> result = adminUserRepository.findByUsername("admin");

        // 検証
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUsername());
        assertEquals(User.UserRole.ADMIN, result.get().getRole());

        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        verify(userQuery).setParameter("username", "admin");
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0002_findByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenThrow(new NoResultException());

        // テスト実行
        Optional<User> result = adminUserRepository.findByUsername("nonExistentUser");

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        verify(userQuery).setParameter("username", "nonExistentUser");
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0003_findByUsername_異常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // テスト実行
        Optional<User> result = adminUserRepository.findByUsername("invalidUser");

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        verify(userQuery).setParameter("username", "invalidUser");
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0004_findAdminByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenReturn(testUser);

        // テスト実行
        Optional<User> result = adminUserRepository.findAdminByUsername("admin");

        // 検証
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUsername());
        assertEquals(User.UserRole.ADMIN, result.get().getRole());

        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.username = :username AND u.role = :role",
                User.class);
        verify(userQuery).setParameter("username", "admin");
        verify(userQuery).setParameter("role", User.UserRole.ADMIN);
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0005_findAdminByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenThrow(new NoResultException());

        // テスト実行
        Optional<User> result = adminUserRepository.findAdminByUsername("nonExistentAdmin");

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.username = :username AND u.role = :role",
                User.class);
        verify(userQuery).setParameter("username", "nonExistentAdmin");
        verify(userQuery).setParameter("role", User.UserRole.ADMIN);
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0006_findAdminByUsername_異常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // テスト実行
        Optional<User> result = adminUserRepository.findAdminByUsername("invalidAdmin");

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery("SELECT u FROM User u WHERE u.username = :username AND u.role = :role",
                User.class);
        verify(userQuery).setParameter("username", "invalidAdmin");
        verify(userQuery).setParameter("role", User.UserRole.ADMIN);
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0007_findEnabledAdminByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenReturn(testUser);

        // テスト実行
        Optional<User> result = adminUserRepository.findEnabledAdminByUsername("enabledAdmin");

        // 検証
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUsername());
        assertEquals(User.UserRole.ADMIN, result.get().getRole());
        assertTrue(result.get().getEnabled());

        verify(entityManager).createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.role = :role AND u.enabled = :enabled",
                User.class);
        verify(userQuery).setParameter("username", "enabledAdmin");
        verify(userQuery).setParameter("role", User.UserRole.ADMIN);
        verify(userQuery).setParameter("enabled", true);
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0008_findEnabledAdminByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenThrow(new NoResultException());

        // テスト実行
        Optional<User> result = adminUserRepository.findEnabledAdminByUsername("disabledAdmin");

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.role = :role AND u.enabled = :enabled",
                User.class);
        verify(userQuery).setParameter("username", "disabledAdmin");
        verify(userQuery).setParameter("role", User.UserRole.ADMIN);
        verify(userQuery).setParameter("enabled", true);
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0009_findEnabledAdminByUsername_異常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), any())).thenReturn(userQuery);
        when(userQuery.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // テスト実行
        Optional<User> result = adminUserRepository.findEnabledAdminByUsername("invalidEnabledAdmin");

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.role = :role AND u.enabled = :enabled",
                User.class);
        verify(userQuery).setParameter("username", "invalidEnabledAdmin");
        verify(userQuery).setParameter("role", User.UserRole.ADMIN);
        verify(userQuery).setParameter("enabled", true);
        verify(userQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0010_updateLastLogin_正常系() {
        // モックの設定
        when(entityManager.find(User.class, 1L)).thenReturn(testUser);
        when(entityManager.merge(any(User.class))).thenReturn(testUser);

        // テスト実行
        Optional<User> result = adminUserRepository.updateLastLogin(1L);

        // 検証
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getUsername());

        verify(entityManager).find(User.class, 1L);
        verify(entityManager).merge(any(User.class));
    }

    @Test
    void UT_Repository_0011_updateLastLogin_正常系() {
        // モックの設定
        when(entityManager.find(User.class, 999L)).thenReturn(null);

        // テスト実行
        Optional<User> result = adminUserRepository.updateLastLogin(999L);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).find(User.class, 999L);
        verify(entityManager, never()).merge(any(User.class));
    }

    @Test
    void UT_Repository_0012_existsByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = adminUserRepository.existsByUsername("admin");

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
        verify(longQuery).setParameter("username", "admin");
        verify(longQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0013_existsByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = adminUserRepository.existsByUsername("nonExistentUser");

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
        verify(longQuery).setParameter("username", "nonExistentUser");
        verify(longQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0014_existsAdminByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = adminUserRepository.existsAdminByUsername("admin");

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username AND u.role = :role",
                Long.class);
        verify(longQuery).setParameter("username", "admin");
        verify(longQuery).setParameter("role", User.UserRole.ADMIN);
        verify(longQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0015_existsAdminByUsername_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = adminUserRepository.existsAdminByUsername("nonExistentAdmin");

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username AND u.role = :role",
                Long.class);
        verify(longQuery).setParameter("username", "nonExistentAdmin");
        verify(longQuery).setParameter("role", User.UserRole.ADMIN);
        verify(longQuery).getSingleResult();
    }
}