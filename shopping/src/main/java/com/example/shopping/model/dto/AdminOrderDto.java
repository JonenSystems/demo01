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
 * 管理者注文DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDto {

    private Long id;
    private String orderNumber;
    private AdminCustomerDto customer;
    private List<AdminOrderDetailDto> orderDetails;
    private BigDecimal totalAmount;
    private Order.OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * EntityからDTOへの変換
     * 
     * @param order 注文エンティティ
     * @return 注文DTO
     */
    public static AdminOrderDto fromEntity(Order order) {
        if (order == null) {
            return null;
        }

        AdminOrderDto dto = new AdminOrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomer(AdminCustomerDto.fromEntity(order.getCustomer()));
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        // 注文詳細の変換
        if (order.getOrderDetails() != null) {
            List<AdminOrderDetailDto> detailDtos = order.getOrderDetails().stream()
                    .map(AdminOrderDetailDto::fromEntity)
                    .collect(Collectors.toList());
            dto.setOrderDetails(detailDtos);
        }

        return dto;
    }

    /**
     * DTOからEntityへの変換
     * 
     * @return 注文エンティティ
     */
    public Order toEntity() {
        Order entity = new Order();
        entity.setId(this.id);
        entity.setOrderNumber(this.orderNumber);
        entity.setCustomer(this.customer != null ? this.customer.toEntity() : null);
        entity.setTotalAmount(this.totalAmount);
        entity.setStatus(this.status);
        return entity;
    }
}