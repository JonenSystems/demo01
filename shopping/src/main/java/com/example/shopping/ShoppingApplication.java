package com.example.shopping;

import com.example.shopping.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * ショッピングアプリケーションのメインクラス
 */
@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class ShoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);
	}
}
