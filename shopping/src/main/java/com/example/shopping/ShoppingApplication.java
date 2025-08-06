package com.example.shopping;

import com.example.shopping.common.impl.FileUploadServiceImpl;
import com.example.shopping.common.DatabaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * ショッピングアプリケーションのメインクラス
 */
@SpringBootApplication
@EnableConfigurationProperties({ FileUploadServiceImpl.class, DatabaseConfig.class })
public class ShoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);
	}
}
