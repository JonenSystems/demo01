package com.example.shopping.common;

import com.example.shopping.model.dto.UserCustomerDto;
import com.example.shopping.model.form.UserCustomerForm;
import jakarta.servlet.http.HttpSession;

/**
 * 顧客セッション管理インターフェース
 */
public interface SessionCustomer {

    /**
     * 顧客情報を取得
     * 
     * @param session HttpSession
     * @return 顧客情報
     */
    UserCustomerDto getCustomer(HttpSession session);

    /**
     * 顧客情報を保存
     * 
     * @param customer 顧客情報
     * @param session  HttpSession
     */
    void setCustomer(UserCustomerDto customer, HttpSession session);

    /**
     * 顧客情報をクリア
     * 
     * @param session HttpSession
     */
    void clearCustomer(HttpSession session);

    /**
     * 顧客情報が存在するかチェック
     * 
     * @param session HttpSession
     * @return 存在する場合true
     */
    boolean hasCustomer(HttpSession session);

    /**
     * セッションから顧客Formを取得
     * 
     * @param session HttpSession
     * @return 顧客Form
     */
    UserCustomerForm getCustomerForm(HttpSession session);

    /**
     * 顧客Formをセッションに保存
     * 
     * @param customerForm 顧客Form
     * @param session      HttpSession
     */
    void setCustomerForm(UserCustomerForm customerForm, HttpSession session);
}