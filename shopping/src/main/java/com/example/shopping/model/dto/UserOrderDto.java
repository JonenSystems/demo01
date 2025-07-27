package com.example.shopping.model.dto;

import com.example.shopping.model.entity.Order;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注文DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderDto {

    private Long id;
    private String orderNumber;
    private UserCustomerDto customer;
    private List<UserCartItemDto> orderItems;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;

    /**
     * EntityからDTOへの変換
     * 
     * @param order 注文エンティティ
     * @return 注文DTO
     */
    public static UserOrderDto fromEntity(Order order) {
        if (order == null) {
            return null;
        }

        UserOrderDto dto = new UserOrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomer(UserCustomerDto.fromEntity(order.getCustomer()));
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        dto.setCreatedAt(order.getCreatedAt());

        // 注文詳細の変換
        if (order.getOrderDetails() != null) {
            List<UserCartItemDto> orderItems = order.getOrderDetails().stream()
                    .map(detail -> {
                        UserCartItemDto item = new UserCartItemDto();
                        item.setProductId(detail.getProduct().getId());
                        item.setProductName(detail.getProduct().getName());
                        item.setProductImagePath(detail.getProduct().getImagePath());
                        item.setUnitPrice(detail.getUnitPrice());
                        item.setQuantity(detail.getQuantity());
                        item.setSubtotal(detail.getSubtotal());
                        return item;
                    })
                    .collect(Collectors.toList());
            dto.setOrderItems(orderItems);
        }

        return dto;
    }
}