package com.example.shopping.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * パスワードユーティリティクラス
 */
public class PasswordUtil {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        // admin123のハッシュを生成
        String adminPassword = "admin123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("admin123 → " + adminHash);

        // passwordのハッシュを生成
        String userPassword = "password";
        String userHash = encoder.encode(userPassword);
        System.out.println("password → " + userHash);

        // 検証テスト
        System.out.println("admin123 matches: " + encoder.matches(adminPassword, adminHash));
        System.out.println("password matches: " + encoder.matches(userPassword, userHash));
    }
}