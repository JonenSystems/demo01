package com.example.shopping.model.dto;

import com.example.shopping.model.entity.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 顧客DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCustomerDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;

    /**
     * EntityからDTOへの変換
     * 
     * @param customer 顧客エンティティ
     * @return 顧客DTO
     */
    public static UserCustomerDto fromEntity(Customer customer) {
        if (customer == null) {
            return null;
        }

        UserCustomerDto dto = new UserCustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        return dto;
    }

    /**
     * DTOからEntityへの変換
     * 
     * @return 顧客エンティティ
     */
    public Customer toEntity() {
        Customer entity = new Customer();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setEmail(this.email);
        entity.setPhone(this.phone);
        entity.setAddress(this.address);
        return entity;
    }
}