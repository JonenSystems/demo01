package com.example.shopping.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Userエンティティの単体テスト
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("admin");
        user.setPassword("hashedPassword");
        user.setRole(User.UserRole.ADMIN);
        user.setEnabled(true);
    }

    @Test
    void UT_Entity_0012_onCreate_正常系() {
        // テスト実行前の状態確認
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());

        // テスト実行
        user.onCreate();

        // 検証
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());

        LocalDateTime now = LocalDateTime.now();
        assertTrue(user.getCreatedAt().isBefore(now.plusSeconds(1)) ||
                user.getCreatedAt().isEqual(now));
        assertTrue(user.getUpdatedAt().isBefore(now.plusSeconds(1)) ||
                user.getUpdatedAt().isEqual(now));
    }

    @Test
    void UT_Entity_0013_onUpdate_正常系() {
        // 初期状態設定
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
        user.setCreatedAt(pastTime);
        user.setUpdatedAt(pastTime);

        // テスト実行前の状態確認
        assertEquals(pastTime, user.getCreatedAt());
        assertEquals(pastTime, user.getUpdatedAt());

        // テスト実行
        user.onUpdate();

        // 検証
        assertEquals(pastTime, user.getCreatedAt()); // createdAtは変更されない
        assertNotNull(user.getUpdatedAt());

        LocalDateTime now = LocalDateTime.now();
        assertTrue(user.getUpdatedAt().isAfter(pastTime));
        assertTrue(user.getUpdatedAt().isBefore(now.plusSeconds(1)) ||
                user.getUpdatedAt().isEqual(now));
    }

    @Test
    void UT_Entity_0014_getAuthorities_正常系() {
        // テスト実行
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // 検証
        assertNotNull(authorities);
        assertEquals(1, authorities.size());

        SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        assertTrue(authorities.contains(expectedAuthority));
    }

    @Test
    void UT_Entity_0015_getAuthorities_正常系() {
        // USERロールに設定
        user.setRole(User.UserRole.USER);

        // テスト実行
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // 検証
        assertNotNull(authorities);
        assertEquals(1, authorities.size());

        SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        assertTrue(authorities.contains(expectedAuthority));
    }

    @Test
    void UT_Entity_0016_isAccountNonExpired_正常系() {
        // 有効なアカウントに設定
        user.setEnabled(true);

        // テスト実行
        boolean result = user.isAccountNonExpired();

        // 検証
        assertTrue(result);
    }

    @Test
    void UT_Entity_0017_isAccountNonExpired_正常系() {
        // 無効なアカウントに設定
        user.setEnabled(false);

        // テスト実行
        boolean result = user.isAccountNonExpired();

        // 検証
        assertTrue(result); // isAccountNonExpiredは常にtrueを返す
    }
}