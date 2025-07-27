package com.example.shopping.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * パスワード暗号化ユーティリティクラス
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    /**
     * パスワードをBCryptで暗号化する
     * 
     * @param rawPassword 平文パスワード
     * @return 暗号化されたパスワード
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("パスワードは空にできません");
        }
        return encoder.encode(rawPassword);
    }

    /**
     * パスワードが一致するかどうかを検証する
     * 
     * @param rawPassword     平文パスワード
     * @param encodedPassword 暗号化されたパスワード
     * @return 一致する場合はtrue
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * パスワードが有効な形式かどうかを検証する
     * 
     * @param password パスワード
     * @return 有効な場合はtrue
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        // 最小8文字、最大128文字
        if (password.length() < 8 || password.length() > 128) {
            return false;
        }

        // 英数字を含む（簡易チェック）
        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasLetter && hasDigit;
    }

    /**
     * 開発環境用：パスワード検証テスト
     * 
     * @param args コマンドライン引数（使用しない）
     */
    public static void main(String[] args) {
        System.out.println("=== パスワード暗号化テスト ===");

        // テスト用パスワード
        String testPassword = "password";

        // 暗号化
        String encodedPassword = encode(testPassword);
        System.out.println("平文パスワード: " + testPassword);
        System.out.println("暗号化パスワード: " + encodedPassword);

        // 検証
        boolean isValid = matches(testPassword, encodedPassword);
        System.out.println("検証結果: " + (isValid ? "成功" : "失敗"));

        // 間違ったパスワードでの検証
        boolean isInvalid = matches("wrongpassword", encodedPassword);
        System.out.println("間違ったパスワード検証: " + (isInvalid ? "成功" : "失敗"));

        // パスワード強度チェック
        System.out.println("\n=== パスワード強度チェック ===");
        String[] testPasswords = {
                "password", // 有効
                "12345678", // 数字のみ（無効）
                "abcdefgh", // 文字のみ（無効）
                "pass", // 短すぎる（無効）
                "", // 空（無効）
                null // null（無効）
        };

        for (String password : testPasswords) {
            boolean valid = isValidPassword(password);
            System.out.println("パスワード: " + (password == null ? "null" : password) +
                    " -> " + (valid ? "有効" : "無効"));
        }

        // 管理者用パスワードの生成例
        System.out.println("\n=== 管理者用パスワード生成例 ===");
        String adminPassword = "admin123";
        String adminEncoded = encode(adminPassword);
        System.out.println("管理者パスワード: " + adminPassword);
        System.out.println("暗号化結果: " + adminEncoded);
        System.out.println("SQL挿入用: INSERT INTO users (username, password, role, enabled) VALUES ('admin', '"
                + adminEncoded + "', 'ADMIN', true);");
    }
}