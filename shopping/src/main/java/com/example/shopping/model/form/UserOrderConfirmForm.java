package com.example.shopping.model.form;

import com.example.shopping.model.dto.UserCartItemDto;
import com.example.shopping.model.dto.UserCustomerDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * ユーザー注文確認Formクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderConfirmForm {

    private List<UserCartItemDto> cartItems;
    private UserCustomerDto customer;
    private BigDecimal totalAmount;
    private String orderNumber;

    /**
     * カートアイテムが存在するかチェック
     * 
     * @return カートアイテムが存在する場合true
     */
    public boolean hasCartItems() {
        return cartItems != null && !cartItems.isEmpty();
    }

    /**
     * カートアイテム数を取得
     * 
     * @return カートアイテム数
     */
    public int getCartItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    /**
     * 顧客情報が入力されているかチェック
     * 
     * @return 顧客情報が入力されている場合true
     */
    public boolean hasCustomerInfo() {
        return customer != null && customer.getName() != null && !customer.getName().trim().isEmpty();
    }
}