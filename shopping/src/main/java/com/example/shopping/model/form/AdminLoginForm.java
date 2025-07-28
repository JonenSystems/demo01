package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminUserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理者ログインフォーム
 */
@Data
public class AdminLoginForm {

    @NotBlank(message = "ユーザー名を入力してください")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    private String username;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 1, max = 255, message = "パスワードを入力してください")
    private String password;

    /**
     * DTOに変換するメソッド
     * 
     * @return AdminUserDto
     */
    public AdminUserDto toDto() {
        AdminUserDto dto = new AdminUserDto();
        dto.setUsername(this.username);
        dto.setPassword(this.password);
        return dto;
    }

    /**
     * DTOからフォームに変換するメソッド
     * 
     * @param dto AdminUserDto
     * @return AdminLoginForm
     */
    public static AdminLoginForm fromDto(AdminUserDto dto) {
        AdminLoginForm form = new AdminLoginForm();
        if (dto != null) {
            form.setUsername(dto.getUsername());
        }
        // セキュリティのため、パスワードは設定しない
        return form;
    }
}