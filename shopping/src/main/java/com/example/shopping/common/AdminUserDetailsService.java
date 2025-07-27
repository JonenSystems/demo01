package com.example.shopping.common;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 管理者ユーザー詳細サービスインターフェース
 * Spring SecurityのUserDetailsServiceを拡張
 */
public interface AdminUserDetailsService extends UserDetailsService {

    /**
     * ユーザー名でユーザー詳細情報を取得する
     * 
     * @param username ユーザー名
     * @return UserDetails（存在しない場合はUserNotFoundException）
     */
    @Override
    org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username);
}