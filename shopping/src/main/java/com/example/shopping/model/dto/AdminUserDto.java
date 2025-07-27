package com.example.shopping.model.dto;

import com.example.shopping.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理者ユーザーDTO
 */
@Data
public class AdminUserDto {

    private Long id;
    private String username;
    private String password; // 認証時のみ使用、通常は設定しない
    private User.UserRole role;
    private Boolean enabled;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * EntityからDTOに変換するメソッド
     * 
     * @param user Userエンティティ
     * @return AdminUserDto
     */
    public static AdminUserDto fromEntity(User user) {
        AdminUserDto dto = new AdminUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        // セキュリティのため、パスワードは設定しない
        dto.setRole(user.getRole());
        dto.setEnabled(user.getEnabled());
        dto.setLastLogin(user.getLastLogin());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    /**
     * DTOからEntityに変換するメソッド
     * 
     * @return Userエンティティ
     */
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setRole(this.role);
        user.setEnabled(this.enabled);
        user.setLastLogin(this.lastLogin);
        user.setCreatedAt(this.createdAt);
        user.setUpdatedAt(this.updatedAt);
        return user;
    }

    /**
     * 管理者権限を持つかどうかを判定
     * 
     * @return 管理者権限を持つ場合はtrue
     */
    public boolean isAdmin() {
        return User.UserRole.ADMIN.equals(this.role);
    }

    /**
     * 有効なユーザーかどうかを判定
     * 
     * @return 有効な場合はtrue
     */
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.enabled);
    }
}