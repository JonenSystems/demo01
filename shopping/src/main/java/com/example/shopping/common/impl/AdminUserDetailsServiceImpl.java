package com.example.shopping.common.impl;

import com.example.shopping.common.AdminUserDetailsService;
import com.example.shopping.model.entity.User;
import com.example.shopping.repository.AdminUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 管理者ユーザー詳細サービス実装クラス
 */
@Service
@Slf4j
public class AdminUserDetailsServiceImpl implements AdminUserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserDetailsServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("認証リクエスト: username={}", username);
        
        // 有効な管理者ユーザーを検索
        Optional<User> userOpt = adminUserRepository.findEnabledAdminByUsername(username);
        
        log.debug("ユーザー検索結果: found={}", userOpt.isPresent());

        if (userOpt.isEmpty()) {
            log.warn("管理者ユーザーが見つかりません: {}", username);
            throw new UsernameNotFoundException("管理者ユーザーが見つかりません: " + username);
        }

        User user = userOpt.get();
        log.debug("ユーザー情報: id={}, username={}, role={}, enabled={}", 
                user.getId(), user.getUsername(), user.getRole(), user.isEnabled());

        // ユーザーが無効な場合
        if (!user.isEnabled()) {
            log.warn("ユーザーが無効です: {}", username);
            throw new UsernameNotFoundException("ユーザーが無効です: " + username);
        }

        // 管理者権限がない場合
        if (!User.UserRole.ADMIN.equals(user.getRole())) {
            log.warn("管理者権限がありません: {}", username);
            throw new UsernameNotFoundException("管理者権限がありません: " + username);
        }

        log.debug("認証成功: {}", username);
        // UserDetailsを返す（UserエンティティがUserDetailsを実装しているため直接返せる）
        return user;
    }
}