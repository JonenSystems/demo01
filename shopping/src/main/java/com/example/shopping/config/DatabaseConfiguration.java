package com.example.shopping.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * データベース設定クラス
 */
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "app.database", name = "url")
public class DatabaseConfiguration {

    @Autowired
    private AppConfig appConfig;

    /**
     * データソースプロパティを設定
     */
    @Bean
    @Primary
    @ConfigurationProperties("app.database")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        AppConfig.Database database = appConfig.getDatabase();
        properties.setUrl(database.getUrl());
        properties.setDriverClassName(database.getDriverClassName());
        properties.setUsername(database.getUsername());
        properties.setPassword(database.getPassword());

        log.info("カスタムデータベース設定を読み込みました: {}", database.getUrl());
        return properties;
    }

    /**
     * データソースを設定
     */
    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
}