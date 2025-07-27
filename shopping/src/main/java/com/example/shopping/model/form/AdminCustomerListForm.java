package com.example.shopping.model.form;

import com.example.shopping.model.dto.AdminCustomerDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 管理者顧客一覧Formクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCustomerListForm {

    private List<AdminCustomerDto> customers;
    private String searchName;
    private String searchEmail;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;

    /**
     * DTOリストからFormへの変換
     * 
     * @param customers 顧客DTOリスト
     * @return 顧客一覧Form
     */
    public static AdminCustomerListForm fromDtoList(List<AdminCustomerDto> customers) {
        AdminCustomerListForm form = new AdminCustomerListForm();
        form.setCustomers(customers);
        return form;
    }

    /**
     * 顧客が存在するかチェック
     * 
     * @return 顧客が存在する場合true
     */
    public boolean hasCustomers() {
        return customers != null && !customers.isEmpty();
    }

    /**
     * 顧客数を取得
     * 
     * @return 顧客数
     */
    public int getCustomerCount() {
        return customers != null ? customers.size() : 0;
    }
}