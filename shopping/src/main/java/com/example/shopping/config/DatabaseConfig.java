package com.example.shopping.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * データベース設定クラス（非推奨 - AppConfigを使用してください）
 */
@Component
@Data
public class DatabaseConfig {

    /**
     * データベースURL
     */
    private String url;

    /**
     * データベースドライバー
     */
    private String driverClassName;

    /**
     * データベースユーザー名
     */
    private String username;

    /**
     * データベースパスワード
     */
    private String password;

    /**
     * データベースプラットフォーム
     */
    private String platform;

    /**
     * DDL自動生成設定
     */
    private String ddlAuto;

    /**
     * SQL表示設定
     */
    private boolean showSql;
}