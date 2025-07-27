package com.example.shopping.model.form;

import com.example.shopping.model.dto.UserCartItemDto;
import com.example.shopping.model.dto.UserCustomerDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ユーザー注文Formクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderForm {

    private String orderNumber;
    private UserCustomerDto customer;
    private List<UserCartItemDto> orderItems;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    /**
     * DTOからFormへの変換
     * 
     * @param orderDto 注文DTO
     * @return 注文Form
     */
    public static UserOrderForm fromDto(com.example.shopping.model.dto.UserOrderDto orderDto) {
        if (orderDto == null) {
            return new UserOrderForm();
        }

        UserOrderForm form = new UserOrderForm();
        form.setOrderNumber(orderDto.getOrderNumber());
        form.setCustomer(orderDto.getCustomer());
        form.setOrderItems(orderDto.getOrderItems());
        form.setTotalAmount(orderDto.getTotalAmount());
        form.setCreatedAt(orderDto.getCreatedAt());
        return form;
    }

    /**
     * FormからDTOへの変換
     * 
     * @return 注文DTO
     */
    public com.example.shopping.model.dto.UserOrderDto toDto() {
        com.example.shopping.model.dto.UserOrderDto dto = new com.example.shopping.model.dto.UserOrderDto();
        dto.setOrderNumber(this.orderNumber);
        dto.setCustomer(this.customer);
        dto.setOrderItems(this.orderItems);
        dto.setTotalAmount(this.totalAmount);
        dto.setCreatedAt(this.createdAt);
        return dto;
    }

    /**
     * 注文アイテム数取得
     * 
     * @return アイテム数
     */
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }
}