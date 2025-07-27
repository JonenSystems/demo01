package com.example.shopping.service;

import com.example.shopping.model.dto.UserCartItemDto;

import java.util.List;

/**
 * ユーザーカートServiceインターフェース
 */
public interface UserCartService {

    /**
     * カート内の商品一覧を取得
     * 
     * @return カート内商品一覧
     */
    List<UserCartItemDto> getCartItems();

    /**
     * カートに商品を追加
     * 
     * @param productId 商品ID
     * @param quantity  数量
     * @return 追加成功の場合true
     */
    boolean addToCart(Long productId, Integer quantity);

    /**
     * カートから商品を削除
     * 
     * @param productId 商品ID
     * @return 削除成功の場合true
     */
    boolean removeFromCart(Long productId);

    /**
     * カート内の商品数量を更新
     * 
     * @param productId 商品ID
     * @param quantity  新しい数量
     * @return 更新成功の場合true
     */
    boolean updateCartItemQuantity(Long productId, Integer quantity);

    /**
     * カートをクリア
     * 
     * @return クリア成功の場合true
     */
    boolean clearCart();

    /**
     * カート内の商品数を取得
     * 
     * @return 商品数
     */
    int getCartItemCount();

    /**
     * カート内の合計金額を取得
     * 
     * @return 合計金額
     */
    java.math.BigDecimal getCartTotal();

    /**
     * カートが空かどうかチェック
     * 
     * @return 空の場合true
     */
    boolean isCartEmpty();

    /**
     * カートアイテムに商品情報を設定
     * 
     * @param cartItems カートアイテムリスト
     * @return 商品情報が設定されたカートアイテムリスト
     */
    List<UserCartItemDto> enrichCartItemsWithProductInfo(List<UserCartItemDto> cartItems);
}