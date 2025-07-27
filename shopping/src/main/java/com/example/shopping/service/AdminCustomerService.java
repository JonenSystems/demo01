package com.example.shopping.service;

import com.example.shopping.model.dto.AdminCustomerDto;
import com.example.shopping.model.form.AdminCustomerListForm;
import org.springframework.data.domain.Pageable;

/**
 * 管理者顧客Serviceインターフェース
 */
public interface AdminCustomerService {

    /**
     * 顧客一覧を取得（ページング対応）
     * 
     * @param searchName  顧客名検索
     * @param searchEmail メールアドレス検索
     * @param pageable    ページング情報
     * @return 顧客一覧Form
     */
    AdminCustomerListForm getCustomerList(String searchName, String searchEmail, Pageable pageable);

    /**
     * 顧客詳細を取得
     * 
     * @param customerId 顧客ID
     * @return 顧客DTO
     */
    AdminCustomerDto getCustomerById(Long customerId);

    /**
     * 顧客を保存
     * 
     * @param customerDto 顧客DTO
     * @return 保存成功の場合true
     */
    boolean saveCustomer(AdminCustomerDto customerDto);

    /**
     * 顧客を削除
     * 
     * @param customerId 顧客ID
     * @return 削除成功の場合true
     */
    boolean deleteCustomer(Long customerId);

    /**
     * メールアドレスの重複チェック
     * 
     * @param email     メールアドレス
     * @param excludeId 除外する顧客ID
     * @return 重複する場合true
     */
    boolean isEmailDuplicate(String email, Long excludeId);
}