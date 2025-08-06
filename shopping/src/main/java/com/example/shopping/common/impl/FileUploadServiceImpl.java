package com.example.shopping.common.impl;

import com.example.shopping.common.FileUploadService;
import com.example.shopping.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * ファイルアップロードサービス実装クラス
 */
@Component
@Slf4j
public class FileUploadServiceImpl implements FileUploadService, ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private Environment environment;

    // 許可する画像形式
    private static final String[] ALLOWED_EXTENSIONS = { ".jpg", ".jpeg", ".png", ".gif", ".webp" };

    /**
     * アップロードディレクトリを取得
     */
    private String getUploadDir() {
        return appConfig != null && appConfig.getUpload() != null ? appConfig.getUpload().getDir() : null;
    }

    /**
     * アプリケーション起動完了時にアップロードディレクトリを初期化する
     */
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        initUploadDir();
    }

    /**
     * アップロードディレクトリを初期化する
     */
    private void initUploadDir() {
        // Spring BootのEnvironmentからプロファイルを取得
        String[] activeProfiles = environment.getActiveProfiles();
        String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : "dev";

        log.info("Spring Boot Environment から取得したアクティブプロファイル: {}", activeProfile);
        log.info("設定ファイルから取得したアップロードディレクトリ: {}", getUploadDir());
    }

    /**
     * ファイルをアップロードする
     * 
     * @param file アップロードするファイル
     * @return アップロードされたファイルのパス
     * @throws IOException ファイル操作エラー
     */
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("ファイルが選択されていません");
        }

        // ファイル形式の検証
        String originalFilename = file.getOriginalFilename();
        if (!isValidImageFile(originalFilename)) {
            throw new IllegalArgumentException("許可されていないファイル形式です");
        }

        // アップロードディレクトリの作成
        Path uploadPath = Paths.get(getUploadDir());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // ファイル名の生成（重複を避けるためUUIDを使用）
        String fileExtension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // ファイルの保存
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("ファイルがアップロードされました: {}", filePath);

        // 相対パスを返す（Webからアクセス可能なパス）
        return "/images/product-images/" + fileName;
    }

    /**
     * ファイルを削除する
     * 
     * @param imagePath 削除するファイルのパス
     * @return 削除成功の場合true
     */
    @Override
    public boolean deleteFile(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return false;
        }

        try {
            // /images/product-images/ファイル名 の形式から実際のファイルパスを取得
            String fileName = imagePath.replace("/images/product-images/", "");
            Path filePath = Paths.get(getUploadDir(), fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("ファイルが削除されました: {}", filePath);
                return true;
            }
        } catch (IOException e) {
            log.error("ファイル削除中にエラーが発生しました: {}", e.getMessage());
        }

        return false;
    }

    /**
     * 画像ファイルかどうかを判定する
     * 
     * @param filename ファイル名
     * @return 画像ファイルの場合true
     */
    private boolean isValidImageFile(String filename) {
        if (filename == null) {
            return false;
        }

        String extension = getFileExtension(filename).toLowerCase();
        for (String allowedExtension : ALLOWED_EXTENSIONS) {
            if (allowedExtension.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ファイル拡張子を取得する
     * 
     * @param filename ファイル名
     * @return ファイル拡張子
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}