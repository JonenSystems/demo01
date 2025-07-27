package com.example.shopping.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * ユーザーエンティティ
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private java.time.LocalDateTime lastLogin;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }

    // UserDetailsインターフェースの実装メソッド

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // アカウントの有効期限は設定しない
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // アカウントロック機能は将来的に実装予定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // パスワードの有効期限は設定しない
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.enabled);
    }

    /**
     * ユーザーロール列挙型
     */
    public enum UserRole {
        ADMIN("管理者"),
        USER("一般ユーザー");

        private final String displayName;

        UserRole(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}