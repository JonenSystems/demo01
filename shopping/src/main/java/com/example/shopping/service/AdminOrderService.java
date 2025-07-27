package com.example.shopping.service;

import com.example.shopping.model.dto.AdminOrderDto;
import com.example.shopping.model.form.AdminOrderListForm;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 管理者注文Serviceインターフェース
 */
public interface AdminOrderService {

    /**
     * 注文一覧を取得（ページング対応）
     * 
     * @param searchOrderNumber  注文番号検索
     * @param searchCustomerName 顧客名検索
     * @param searchStatus       ステータス検索
     * @param pageable           ページング情報
     * @return 注文一覧Form
     */
    AdminOrderListForm getOrderList(String searchOrderNumber, String searchCustomerName, String searchStatus,
            Pageable pageable);

    /**
     * 注文詳細を取得
     * 
     * @param orderId 注文ID
     * @return 注文DTO
     */
    AdminOrderDto getOrderById(Long orderId);

    /**
     * 注文ステータスを更新
     * 
     * @param orderId 注文ID
     * @param status  新しいステータス
     * @return 更新成功の場合true
     */
    boolean updateOrderStatus(Long orderId, String status);

    /**
     * 利用可能なステータス一覧を取得
     * 
     * @return ステータス一覧
     */
    List<String> getAvailableStatuses();

    /**
     * 注文統計情報を取得
     * 
     * @return 統計情報マップ
     */
    Map<String, Object> getOrderStatistics();

    /**
     * 今日の注文数を取得
     * 
     * @return 今日の注文数
     */
    long getTodayOrderCount();
}