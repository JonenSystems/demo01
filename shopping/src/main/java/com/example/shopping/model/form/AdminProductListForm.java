package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理者商品一覧Formクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductListForm {

    private List<AdminProductDto> products;
    private String searchName;
    private String searchCategory;
    private List<String> categories;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;

    /**
     * DTOリストからFormへの変換
     * 
     * @param products 商品DTOリスト
     * @return 商品一覧Form
     */
    public static AdminProductListForm fromDtoList(List<AdminProductDto> products) {
        AdminProductListForm form = new AdminProductListForm();
        form.setProducts(products != null ? products : new ArrayList<>());
        return form;
    }

    /**
     * 商品が存在するかチェック
     * 
     * @return 商品が存在する場合true
     */
    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }

    /**
     * 商品数を取得
     * 
     * @return 商品数
     */
    public int getProductCount() {
        return products != null ? products.size() : 0;
    }
}