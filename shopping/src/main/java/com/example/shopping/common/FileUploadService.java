package com.example.shopping.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * ファイルアップロードサービスインターフェース
 */
public interface FileUploadService {

    /**
     * ファイルをアップロードする
     * 
     * @param file アップロードするファイル
     * @return アップロードされたファイルのパス
     * @throws IOException ファイル操作エラー
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * ファイルを削除する
     * 
     * @param imagePath 削除するファイルのパス
     * @return 削除成功の場合true
     */
    boolean deleteFile(String imagePath);
}