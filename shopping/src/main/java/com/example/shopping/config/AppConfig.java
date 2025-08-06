package com.example.shopping.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * アプリケーション設定クラス
 */
@Component
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = true)
@Data
public class AppConfig {

    /**
     * アップロード設定
     */
    private Upload upload = new Upload();

    /**
     * ログ設定
     */
    private Logs logs = new Logs();

    /**
     * データベース設定
     */
    private Database database = new Database();

    /**
     * アップロード設定クラス
     */
    @Data
    public static class Upload {
        private String dir;
    }

    /**
     * ログ設定クラス
     */
    @Data
    public static class Logs {
        private String dir;
    }

    /**
     * データベース設定クラス
     */
    @Data
    public static class Database {
        private String url;
        private String driverClassName;
        private String username;
        private String password;
        private String platform;
        private String ddlAuto;
        private boolean showSql;
    }
}