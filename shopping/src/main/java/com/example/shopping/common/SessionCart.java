package com.example.shopping.common;

import com.example.shopping.model.dto.UserCartItemDto;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * カートセッション管理インターフェース
 */
public interface SessionCart {

    /**
     * カートアイテム一覧を取得
     * 
     * @param session HttpSession
     * @return カートアイテム一覧
     */
    List<UserCartItemDto> getCartItems(HttpSession session);

    /**
     * カートに商品を追加
     * 
     * @param productId 商品ID
     * @param quantity  数量
     * @param session   HttpSession
     */
    void addToCart(Long productId, Integer quantity, HttpSession session);

    /**
     * カートから商品を削除
     * 
     * @param productId 商品ID
     * @param session   HttpSession
     */
    void removeFromCart(Long productId, HttpSession session);

    /**
     * カート内の商品数量を更新
     * 
     * @param productId 商品ID
     * @param quantity  新しい数量
     * @param session   HttpSession
     */
    void updateCartItemQuantity(Long productId, Integer quantity, HttpSession session);

    /**
     * カートをクリア
     * 
     * @param session HttpSession
     */
    void clearCart(HttpSession session);

    /**
     * カートアイテム数を取得
     * 
     * @param session HttpSession
     * @return カートアイテム数
     */
    int getCartItemCount(HttpSession session);

    /**
     * カート合計金額を取得
     * 
     * @param session HttpSession
     * @return 合計金額
     */
    java.math.BigDecimal getCartTotal(HttpSession session);

    /**
     * カートが空かどうかチェック
     * 
     * @param session HttpSession
     * @return 空の場合true
     */
    boolean isCartEmpty(HttpSession session);
}