package com.example.shopping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web設定クラス
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 静的リソースの設定
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // CSSファイルの設定
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // JavaScriptファイルの設定
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        // 画像ファイルの設定
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/", "file:/opt/shopping/static/images/");

        // その他の静的リソース
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}