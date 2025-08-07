package com.example.shopping.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web設定クラス
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

        private final Environment environment;

        /**
         * コンストラクタインジェクション
         */
        public WebConfig(Environment environment) {
                this.environment = environment;
                log.info("WebConfigが初期化されました");
        }

        /**
         * 静的リソースの設定
         */
        @Override
        public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                log.info("addResourceHandlersメソッドが呼び出されました");

                // CSSファイルの設定
                registry.addResourceHandler("/css/**")
                                .addResourceLocations("classpath:/static/css/");

                // JavaScriptファイルの設定
                registry.addResourceHandler("/js/**")
                                .addResourceLocations("classpath:/static/js/");

                // 画像ファイルの設定（開発環境と本番環境の両方に対応）
                String uploadDir = environment.getProperty("app.upload.dir");
                String imagePath = null;

                log.info("app.upload.dirプロパティの値: {}", uploadDir);

                if (uploadDir != null && !uploadDir.isEmpty()) {
                        // 設定ファイルから取得したディレクトリの親ディレクトリを取得
                        if (uploadDir.endsWith("/product-images")) {
                                imagePath = "file:"
                                                + uploadDir.substring(0, uploadDir.lastIndexOf("/product-images"))
                                                + "/";
                        } else {
                                imagePath = "file:" + uploadDir + "/";
                        }
                        log.info("画像ファイルパスを設定しました: {}", imagePath);
                } else {
                        // 設定が読み込まれない場合はエラーログを出力
                        log.error("app.upload.dirプロパティが設定されていません。画像ファイルが表示されない可能性があります。");
                }

                // 画像ファイルの設定（設定が読み込まれている場合のみ）
                if (imagePath != null) {
                        registry.addResourceHandler("/images/**")
                                        .addResourceLocations(
                                                        "classpath:/static/images/",
                                                        imagePath);
                        log.info("画像ファイルのリソースハンドラーを設定しました: {}", imagePath);
                } else {
                        // 設定が読み込まれない場合は、classpathのみを使用
                        registry.addResourceHandler("/images/**")
                                        .addResourceLocations("classpath:/static/images/");
                        log.warn("画像ファイルのリソースハンドラーをclasspathのみで設定しました。アップロードされた画像は表示されない可能性があります。");
                }

                // その他の静的リソース
                registry.addResourceHandler("/**")
                                .addResourceLocations("classpath:/static/");
        }
}