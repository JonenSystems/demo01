package com.example.shopping.config;

import com.example.shopping.common.DatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
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
    private DatabaseConfig databaseConfig;

    /**
     * データソースプロパティを設定
     */
    @Bean
    @Primary
    @ConfigurationProperties("app.database")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        properties.setUrl(databaseConfig.getUrl());
        properties.setDriverClassName(databaseConfig.getDriverClassName());
        properties.setUsername(databaseConfig.getUsername());
        properties.setPassword(databaseConfig.getPassword());

        log.info("カスタムデータベース設定を読み込みました: {}", databaseConfig.getUrl());
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