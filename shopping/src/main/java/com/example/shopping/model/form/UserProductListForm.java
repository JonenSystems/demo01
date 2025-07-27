package com.example.shopping.model.form;

import com.example.shopping.model.dto.UserProductDto;
import lombok.Data;

import java.util.List;

/**
 * ユーザー商品一覧フォーム
 */
@Data
public class UserProductListForm {

    /** 商品リスト */
    private List<UserProductDto> products;

    /** 選択されたカテゴリ */
    private String selectedCategory;

    /** 利用可能なカテゴリリスト */
    private List<String> categories;

    /**
     * 商品が存在するかどうかをチェック
     * 
     * @return 商品が存在する場合はtrue
     */
    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }
}