package com.example.shopping.service;

import com.example.shopping.model.dto.AdminProductDto;
import com.example.shopping.model.form.AdminProductListForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 管理者商品Serviceインターフェース
 */
public interface AdminProductService {

    /**
     * 商品一覧を取得（ページング対応）
     * 
     * @param searchName     商品名検索
     * @param searchCategory カテゴリ検索
     * @param pageable       ページング情報
     * @return 商品一覧Form
     */
    AdminProductListForm getProductList(String searchName, String searchCategory, Pageable pageable);

    /**
     * 商品詳細を取得
     * 
     * @param productId 商品ID
     * @return 商品DTO
     */
    AdminProductDto getProductById(Long productId);

    /**
     * 商品を保存
     * 
     * @param productDto 商品DTO
     * @return 保存成功の場合true
     */
    boolean saveProduct(AdminProductDto productDto);

    /**
     * 商品を削除
     * 
     * @param productId 商品ID
     * @return 削除成功の場合true
     */
    boolean deleteProduct(Long productId);

    /**
     * 利用可能なカテゴリ一覧を取得
     * 
     * @return カテゴリ一覧
     */
    List<String> getAvailableCategories();

    /**
     * 在庫不足商品一覧を取得
     * 
     * @param threshold 在庫閾値
     * @return 商品一覧
     */
    List<AdminProductDto> getLowStockProducts(Integer threshold);
}