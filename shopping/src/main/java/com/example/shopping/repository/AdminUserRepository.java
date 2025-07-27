package com.example.shopping.repository;

import com.example.shopping.model.entity.User;

import java.util.Optional;

/**
 * 管理者ユーザーリポジトリインターフェース
 */
public interface AdminUserRepository {

    /**
     * ユーザー名でユーザーを検索する
     * 
     * @param username ユーザー名
     * @return ユーザー情報（存在しない場合は空）
     */
    Optional<User> findByUsername(String username);

    /**
     * ユーザー名で管理者ユーザーを検索する
     * 
     * @param username ユーザー名
     * @return 管理者ユーザー情報（存在しない場合は空）
     */
    Optional<User> findAdminByUsername(String username);

    /**
     * ユーザー名で有効な管理者ユーザーを検索する
     * 
     * @param username ユーザー名
     * @return 有効な管理者ユーザー情報（存在しない場合は空）
     */
    Optional<User> findEnabledAdminByUsername(String username);

    /**
     * 最終ログイン時刻を更新する
     * 
     * @param userId ユーザーID
     * @return 更新されたユーザー情報（存在しない場合は空）
     */
    Optional<User> updateLastLogin(Long userId);

    /**
     * ユーザーが存在するかどうかを確認する
     * 
     * @param username ユーザー名
     * @return 存在する場合はtrue
     */
    boolean existsByUsername(String username);

    /**
     * 管理者ユーザーが存在するかどうかを確認する
     * 
     * @param username ユーザー名
     * @return 管理者として存在する場合はtrue
     */
    boolean existsAdminByUsername(String username);
}