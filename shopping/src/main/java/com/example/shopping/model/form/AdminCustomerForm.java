package com.example.shopping.model.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 管理者顧客Formクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCustomerForm {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;

    /**
     * DTOからFormへの変換
     * 
     * @param customerDto 顧客DTO
     * @return 顧客Form
     */
    public static AdminCustomerForm fromDto(com.example.shopping.model.dto.AdminCustomerDto customerDto) {
        if (customerDto == null) {
            return new AdminCustomerForm();
        }

        AdminCustomerForm form = new AdminCustomerForm();
        form.setId(customerDto.getId());
        form.setName(customerDto.getName());
        form.setEmail(customerDto.getEmail());
        form.setPhone(customerDto.getPhone());
        form.setAddress(customerDto.getAddress());
        return form;
    }

    /**
     * FormからDTOへの変換
     * 
     * @return 顧客DTO
     */
    public com.example.shopping.model.dto.AdminCustomerDto toDto() {
        com.example.shopping.model.dto.AdminCustomerDto dto = new com.example.shopping.model.dto.AdminCustomerDto();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setEmail(this.email);
        dto.setPhone(this.phone);
        dto.setAddress(this.address);
        return dto;
    }

    /**
     * FormからEntityへの変換
     * 
     * @return 顧客エンティティ
     */
    public com.example.shopping.model.entity.Customer toEntity() {
        com.example.shopping.model.entity.Customer entity = new com.example.shopping.model.entity.Customer();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setEmail(this.email);
        entity.setPhone(this.phone);
        entity.setAddress(this.address);
        return entity;
    }

    /**
     * 新規顧客かどうか判定
     * 
     * @return 新規顧客の場合true
     */
    public boolean isNew() {
        return id == null;
    }
}