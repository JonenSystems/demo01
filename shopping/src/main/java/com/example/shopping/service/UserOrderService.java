package com.example.shopping.service;

import com.example.shopping.model.dto.UserOrderDto;
import com.example.shopping.model.dto.UserCartItemDto;
import com.example.shopping.model.dto.UserCustomerDto;

import java.util.List;

/**
 * ユーザー注文Serviceインターフェース
 */
public interface UserOrderService {

    /**
     * 注文確認画面用のデータを取得
     * 
     * @return 注文確認DTO
     */
    UserOrderDto getOrderConfirmData();

    /**
     * 顧客情報を保存
     * 
     * @param customerDto 顧客情報DTO
     * @return 保存成功の場合true
     */
    boolean saveCustomerInfo(UserCustomerDto customerDto);

    /**
     * 注文を確定
     * 
     * @return 注文DTO
     */
    UserOrderDto createOrder();

    /**
     * 注文番号で注文を取得
     * 
     * @param orderNumber 注文番号
     * @return 注文DTO
     */
    UserOrderDto getOrderByOrderNumber(String orderNumber);

    /**
     * 顧客IDで注文履歴を取得
     * 
     * @param customerId 顧客ID
     * @return 注文履歴
     */
    List<UserOrderDto> getOrderHistoryByCustomerId(Long customerId);

    /**
     * 注文番号を生成
     * 
     * @return 注文番号
     */
    String generateOrderNumber();

    /**
     * 注文確認データを作成
     * 
     * @param cartItems   カートアイテムリスト
     * @param customerDto 顧客DTO
     * @return 注文確認DTO
     */
    UserOrderDto createOrderConfirmData(List<UserCartItemDto> cartItems, UserCustomerDto customerDto);

    /**
     * 注文をデータベースに保存
     * 
     * @param cartItems   カートアイテムリスト
     * @param customerDto 顧客DTO
     * @return 保存された注文DTO
     */
    UserOrderDto saveOrderToDatabase(List<UserCartItemDto> cartItems, UserCustomerDto customerDto);
}