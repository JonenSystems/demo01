package com.example.shopping.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * カートアイテムDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCartItemDto {

    private Long productId;
    private String productName;
    private String productImagePath;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;

    /**
     * 小計を計算
     */
    public void calculateSubtotal() {
        if (unitPrice != null && quantity != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}