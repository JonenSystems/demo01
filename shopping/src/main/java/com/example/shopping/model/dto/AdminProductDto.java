package com.example.shopping.model.dto;

import com.example.shopping.model.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理者商品DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer stockQuantity;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * EntityからDTOへの変換
     * 
     * @param product 商品エンティティ
     * @return 商品DTO
     */
    public static AdminProductDto fromEntity(Product product) {
        if (product == null) {
            return null;
        }

        AdminProductDto dto = new AdminProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setImagePath(product.getImagePath());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    /**
     * DTOからEntityへの変換
     * 
     * @return 商品エンティティ
     */
    public Product toEntity() {
        Product entity = new Product();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPrice(this.price);
        entity.setCategory(this.category);
        entity.setStockQuantity(this.stockQuantity);
        entity.setImagePath(this.imagePath);
        return entity;
    }
}