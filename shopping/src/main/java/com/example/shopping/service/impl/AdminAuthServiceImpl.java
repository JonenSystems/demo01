package com.example.shopping.service.impl;

import com.example.shopping.model.dto.AdminAuthResultDto;
import com.example.shopping.model.dto.AdminUserDto;
import com.example.shopping.model.entity.User;
import com.example.shopping.repository.AdminUserRepository;
import com.example.shopping.service.AdminAuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 管理者認証サービス実装クラス
 */
@Service
@Transactional
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthServiceImpl(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminAuthResultDto authenticate(String username, String password) {
        // 入力値の検証
        if (username == null || username.trim().isEmpty()) {
            return AdminAuthResultDto.failure(AdminAuthResultDto.AuthResultType.FAILURE, "ユーザー名を入力してください");
        }
        if (password == null || password.trim().isEmpty()) {
            return AdminAuthResultDto.failure(AdminAuthResultDto.AuthResultType.FAILURE, "パスワードを入力してください");
        }

        // 有効な管理者ユーザーを検索
        Optional<User> userOpt = adminUserRepository.findEnabledAdminByUsername(username.trim());

        if (userOpt.isEmpty()) {
            return AdminAuthResultDto.failure(AdminAuthResultDto.AuthResultType.USER_NOT_FOUND);
        }

        User user = userOpt.get();

        // ユーザーが無効な場合
        if (!user.isEnabled()) {
            return AdminAuthResultDto.failure(AdminAuthResultDto.AuthResultType.USER_DISABLED);
        }

        // 管理者権限がない場合
        if (!User.UserRole.ADMIN.equals(user.getRole())) {
            return AdminAuthResultDto.failure(AdminAuthResultDto.AuthResultType.INVALID_ROLE);
        }

        // パスワード検証
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return AdminAuthResultDto.failure(AdminAuthResultDto.AuthResultType.INVALID_PASSWORD);
        }

        // 認証成功 - 最終ログイン時刻を更新
        try {
            adminUserRepository.updateLastLogin(user.getId());
            // 更新されたユーザー情報を取得
            Optional<User> updatedUserOpt = adminUserRepository.findByUsername(username);
            if (updatedUserOpt.isPresent()) {
                AdminUserDto userDto = AdminUserDto.fromEntity(updatedUserOpt.get());
                return AdminAuthResultDto.success(userDto);
            }
        } catch (Exception e) {
            // ログイン時刻更新に失敗しても認証は成功とする
            AdminUserDto userDto = AdminUserDto.fromEntity(user);
            return AdminAuthResultDto.success(userDto);
        }

        // 予期しないエラー
        return AdminAuthResultDto.failure(AdminAuthResultDto.AuthResultType.FAILURE, "認証処理中にエラーが発生しました");
    }

    @Override
    public AdminUserDto updateLastLogin(Long userId) {
        Optional<User> userOpt = adminUserRepository.updateLastLogin(userId);
        return userOpt.map(AdminUserDto::fromEntity).orElse(null);
    }

    @Override
    public AdminUserDto findAdminByUsername(String username) {
        Optional<User> userOpt = adminUserRepository.findAdminByUsername(username);
        return userOpt.map(AdminUserDto::fromEntity).orElse(null);
    }

    @Override
    public boolean existsAdminByUsername(String username) {
        return adminUserRepository.existsAdminByUsername(username);
    }
}