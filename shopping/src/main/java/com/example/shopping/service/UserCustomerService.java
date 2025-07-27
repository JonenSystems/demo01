package com.example.shopping.service;

import com.example.shopping.model.dto.UserCustomerDto;

/**
 * ユーザー顧客Serviceインターフェース
 */
public interface UserCustomerService {

    /**
     * 顧客情報を保存
     * 
     * @param customerDto 顧客DTO
     * @return 保存成功の場合true
     */
    boolean saveCustomer(UserCustomerDto customerDto);

    /**
     * セッションから顧客情報を取得
     * 
     * @return 顧客DTO
     */
    UserCustomerDto getCustomerFromSession();

    /**
     * 顧客情報をセッションに保存
     * 
     * @param customerDto 顧客DTO
     */
    void saveCustomerToSession(UserCustomerDto customerDto);

    /**
     * 顧客情報をセッションからクリア
     */
    void clearCustomerFromSession();
}