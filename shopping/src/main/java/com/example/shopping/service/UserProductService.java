package com.example.shopping.service;

import com.example.shopping.model.dto.UserProductDto;

import java.util.List;

/**
 * ユーザー商品サービス
 */
public interface UserProductService {

    /**
     * 全商品を取得
     * 
     * @return 商品一覧DTO
     */
    List<UserProductDto> getAllProducts();

    /**
     * カテゴリ別商品を取得
     * 
     * @param category カテゴリ
     * @return 商品一覧DTO
     */
    List<UserProductDto> getProductsByCategory(String category);
}