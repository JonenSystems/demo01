package com.example.shopping.model.dto;

import com.example.shopping.model.entity.OrderDetail;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理者注文詳細DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDetailDto {

    private Long id;
    private AdminProductDto product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * EntityからDTOへの変換
     * 
     * @param orderDetail 注文詳細エンティティ
     * @return 注文詳細DTO
     */
    public static AdminOrderDetailDto fromEntity(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }

        AdminOrderDetailDto dto = new AdminOrderDetailDto();
        dto.setId(orderDetail.getId());
        dto.setProduct(AdminProductDto.fromEntity(orderDetail.getProduct()));
        dto.setQuantity(orderDetail.getQuantity());
        dto.setUnitPrice(orderDetail.getUnitPrice());
        dto.setSubtotal(orderDetail.getSubtotal());
        dto.setCreatedAt(orderDetail.getCreatedAt());
        dto.setUpdatedAt(orderDetail.getUpdatedAt());
        return dto;
    }

    /**
     * DTOからEntityへの変換
     * 
     * @return 注文詳細エンティティ
     */
    public OrderDetail toEntity() {
        OrderDetail entity = new OrderDetail();
        entity.setId(this.id);
        entity.setProduct(this.product != null ? this.product.toEntity() : null);
        entity.setQuantity(this.quantity);
        entity.setUnitPrice(this.unitPrice);
        entity.setSubtotal(this.subtotal);
        return entity;
    }
}