package com.example.shopping.model.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 管理者認証結果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthResultDto {

    /**
     * 認証結果の種類
     */
    public enum AuthResultType {
        SUCCESS("認証成功"),
        FAILURE("認証失敗"),
        USER_NOT_FOUND("ユーザーが見つかりません"),
        INVALID_PASSWORD("パスワードが正しくありません"),
        USER_DISABLED("ユーザーが無効です"),
        INVALID_ROLE("管理者権限がありません");

        private final String message;

        AuthResultType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * 認証結果の種類
     */
    private AuthResultType resultType;

    /**
     * 認証成功時のユーザー情報
     */
    private AdminUserDto user;

    /**
     * エラーメッセージ
     */
    private String errorMessage;

    /**
     * 認証成功かどうか
     */
    private boolean success;

    /**
     * 認証成功時の結果を作成
     * 
     * @param user 認証されたユーザー情報
     * @return AdminAuthResultDto
     */
    public static AdminAuthResultDto success(AdminUserDto user) {
        return AdminAuthResultDto.builder()
                .resultType(AuthResultType.SUCCESS)
                .user(user)
                .success(true)
                .build();
    }

    /**
     * 認証失敗時の結果を作成
     * 
     * @param resultType   失敗の種類
     * @param errorMessage エラーメッセージ
     * @return AdminAuthResultDto
     */
    public static AdminAuthResultDto failure(AuthResultType resultType, String errorMessage) {
        return AdminAuthResultDto.builder()
                .resultType(resultType)
                .errorMessage(errorMessage)
                .success(false)
                .build();
    }

    /**
     * 認証失敗時の結果を作成（デフォルトメッセージ）
     * 
     * @param resultType 失敗の種類
     * @return AdminAuthResultDto
     */
    public static AdminAuthResultDto failure(AuthResultType resultType) {
        return failure(resultType, resultType.getMessage());
    }
}