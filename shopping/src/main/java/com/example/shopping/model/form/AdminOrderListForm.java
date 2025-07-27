package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminOrderDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 管理者注文一覧Formクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderListForm {

    private List<AdminOrderDto> orders;
    private String searchOrderNumber;
    private String searchCustomerName;
    private String searchStatus;
    private List<String> statusOptions;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;

    /**
     * DTOリストからFormへの変換
     * 
     * @param orders 注文DTOリスト
     * @return 注文一覧Form
     */
    public static AdminOrderListForm fromDtoList(List<AdminOrderDto> orders) {
        AdminOrderListForm form = new AdminOrderListForm();
        form.setOrders(orders);
        return form;
    }

    /**
     * 注文が存在するかチェック
     * 
     * @return 注文が存在する場合true
     */
    public boolean hasOrders() {
        return orders != null && !orders.isEmpty();
    }

    /**
     * 注文数を取得
     * 
     * @return 注文数
     */
    public int getOrderCount() {
        return orders != null ? orders.size() : 0;
    }
}