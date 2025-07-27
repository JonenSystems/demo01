package com.example.shopping.common;

import com.example.shopping.model.dto.UserOrderDto;
import jakarta.servlet.http.HttpSession;

/**
 * 注文セッション管理インターフェース
 */
public interface SessionOrder {

    /**
     * 注文情報を取得
     * 
     * @param session HttpSession
     * @return 注文情報
     */
    UserOrderDto getOrder(HttpSession session);

    /**
     * 注文情報を保存
     * 
     * @param order   注文情報
     * @param session HttpSession
     */
    void setOrder(UserOrderDto order, HttpSession session);

    /**
     * 注文情報をクリア
     * 
     * @param session HttpSession
     */
    void clearOrder(HttpSession session);

    /**
     * 注文情報が存在するかチェック
     * 
     * @param session HttpSession
     * @return 存在する場合true
     */
    boolean hasOrder(HttpSession session);
}