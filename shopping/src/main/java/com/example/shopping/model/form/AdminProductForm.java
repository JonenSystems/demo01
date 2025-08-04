package com.example.shopping.model.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * 管理者商品Formクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductForm {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer stockQuantity;
    private String imagePath;
    private MultipartFile imageFile;

    /**
     * DTOからFormへの変換
     * 
     * @param productDto 商品DTO
     * @return 商品Form
     */
    public static AdminProductForm fromDto(com.example.shopping.model.dto.AdminProductDto productDto) {
        if (productDto == null) {
            return new AdminProductForm();
        }

        AdminProductForm form = new AdminProductForm();
        form.setId(productDto.getId());
        form.setName(productDto.getName());
        form.setDescription(productDto.getDescription());
        form.setPrice(productDto.getPrice());
        form.setCategory(productDto.getCategory());
        form.setStockQuantity(productDto.getStockQuantity());
        form.setImagePath(productDto.getImagePath());
        return form;
    }

    /**
     * FormからDTOへの変換
     * 
     * @return 商品DTO
     */
    public com.example.shopping.model.dto.AdminProductDto toDto() {
        com.example.shopping.model.dto.AdminProductDto dto = new com.example.shopping.model.dto.AdminProductDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setPrice(this.price);
        dto.setCategory(this.category);
        dto.setStockQuantity(this.stockQuantity);
        dto.setImagePath(this.imagePath);
        return dto;
    }

    /**
     * FormからEntityへの変換
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
        return entity;
    }

    /**
     * 新規商品かどうか判定
     * 
     * @return 新規商品の場合true
     */
    public boolean isNew() {
        return id == null;
    }
}