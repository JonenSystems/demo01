package com.example.shopping.repository;

import com.example.shopping.model.entity.OrderDetail;
import java.util.List;
import java.util.Optional;

/**
 * ユーザー注文詳細Repositoryインターフェース
 */
public interface UserOrderDetailRepository {

    /**
     * 注文IDで注文詳細一覧を取得
     * 
     * @param orderId 注文ID
     * @return 注文詳細一覧
     */
    List<OrderDetail> findByOrderIdOrderByIdAsc(Long orderId);

    /**
     * IDで注文詳細を取得
     * 
     * @param id 注文詳細ID
     * @return 注文詳細（存在しない場合は空）
     */
    Optional<OrderDetail> findById(Long id);

    /**
     * 注文詳細を保存
     * 
     * @param orderDetail 注文詳細
     * @return 保存された注文詳細
     */
    OrderDetail save(OrderDetail orderDetail);

    /**
     * 複数の注文詳細を保存
     * 
     * @param orderDetails 注文詳細リスト
     * @return 保存された注文詳細リスト
     */
    List<OrderDetail> saveAll(List<OrderDetail> orderDetails);

    /**
     * IDで注文詳細を削除
     * 
     * @param id 注文詳細ID
     */
    void deleteById(Long id);

    /**
     * 注文詳細が存在するかチェック
     * 
     * @param id 注文詳細ID
     * @return 存在する場合はtrue
     */
    boolean existsById(Long id);

    /**
     * 全注文詳細を取得
     * 
     * @return 全注文詳細リスト
     */
    List<OrderDetail> findAll();
}