package com.example.shopping.common.impl;

import com.example.shopping.common.AdminUserDetailsService;
import com.example.shopping.model.entity.User;
import com.example.shopping.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 管理者ユーザー詳細サービス実装クラス
 */
@Service
public class AdminUserDetailsServiceImpl implements AdminUserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserDetailsServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 有効な管理者ユーザーを検索
        Optional<User> userOpt = adminUserRepository.findEnabledAdminByUsername(username);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("管理者ユーザーが見つかりません: " + username);
        }

        User user = userOpt.get();

        // ユーザーが無効な場合
        if (!user.isEnabled()) {
            throw new UsernameNotFoundException("ユーザーが無効です: " + username);
        }

        // 管理者権限がない場合
        if (!User.UserRole.ADMIN.equals(user.getRole())) {
            throw new UsernameNotFoundException("管理者権限がありません: " + username);
        }

        // UserDetailsを返す（UserエンティティがUserDetailsを実装しているため直接返せる）
        return user;
    }
}