package com.example.shopping.service;

import com.example.shopping.model.dto.AdminAuthResultDto;
import com.example.shopping.model.dto.AdminUserDto;

/**
 * 管理者認証サービスインターフェース
 */
public interface AdminAuthService {

    /**
     * ユーザー認証を実行する
     * 
     * @param username ユーザー名
     * @param password パスワード
     * @return 認証結果
     */
    AdminAuthResultDto authenticate(String username, String password);

    /**
     * 最終ログイン時刻を更新する
     * 
     * @param userId ユーザーID
     * @return 更新されたユーザー情報（失敗時はnull）
     */
    AdminUserDto updateLastLogin(Long userId);

    /**
     * ユーザー名で管理者ユーザーを検索する
     * 
     * @param username ユーザー名
     * @return 管理者ユーザー情報（存在しない場合はnull）
     */
    AdminUserDto findAdminByUsername(String username);

    /**
     * 管理者ユーザーが存在するかどうかを確認する
     * 
     * @param username ユーザー名
     * @return 管理者として存在する場合はtrue
     */
    boolean existsAdminByUsername(String username);
}