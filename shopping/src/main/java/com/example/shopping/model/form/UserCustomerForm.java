package com.example.shopping.model.form;

import com.example.shopping.model.dto.UserCustomerDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * ユーザー顧客情報Formクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCustomerForm {

    @NotBlank(message = "お名前は必須です")
    @Size(max = 100, message = "お名前は100文字以内で入力してください")
    private String name;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "正しいメールアドレスを入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    private String email;

    @Pattern(regexp = "^[0-9-]*$", message = "電話番号は数字とハイフンのみ入力してください")
    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    private String phone;

    @Size(max = 500, message = "住所は500文字以内で入力してください")
    private String address;

    /**
     * DTOからFormへの変換
     * 
     * @param customerDto 顧客DTO
     * @return 顧客Form
     */
    public static UserCustomerForm fromDto(UserCustomerDto customerDto) {
        if (customerDto == null) {
            return new UserCustomerForm();
        }

        UserCustomerForm form = new UserCustomerForm();
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
    public UserCustomerDto toDto() {
        UserCustomerDto dto = new UserCustomerDto();
        dto.setName(this.name);
        dto.setEmail(this.email);
        dto.setPhone(this.phone);
        dto.setAddress(this.address);
        return dto;
    }
}