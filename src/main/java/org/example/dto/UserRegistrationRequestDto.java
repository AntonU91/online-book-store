package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.example.validation.FieldsMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldsMatch(field = "password", fieldMatch = "repeatPassword",
        message = "Password and repeatPassword fields are not matching")
public class UserRegistrationRequestDto {
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @Length(min = 6)
    private String password;

    @Length(min = 6)
    private String repeatPassword;

    @Length(min = 3)
    private String firstName;

    @NotEmpty
    private String lastName;

    private String shippingAddress;
}
