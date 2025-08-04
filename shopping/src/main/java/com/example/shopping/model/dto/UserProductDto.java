package com.example.shopping.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ユーザー商品DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProductDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer stockQuantity;
    private String imagePath;
    private LocalDateTime updatedAt;

    /**
     * EntityからDTOへの変換
     * 
     * @param product 商品エンティティ
     * @return 商品DTO
     */
    public static UserProductDto fromEntity(com.example.shopping.model.entity.Product product) {
        if (product == null) {
            return null;
        }

        UserProductDto dto = new UserProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setImagePath(product.getImagePath());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    /**
     * DTOからEntityへの変換
     * 
     * @return 商品エンティティ
     */
    public com.example.shopping.model.entity.Product toEntity() {
        com.example.shopping.model.entity.Product entity = new com.example.shopping.model.entity.Product();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPrice(this.price);
        entity.setCategory(this.category);
        entity.setStockQuantity(this.stockQuantity);
        entity.setImagePath(this.imagePath);
        entity.setUpdatedAt(this.updatedAt);
        return entity;
    }
}