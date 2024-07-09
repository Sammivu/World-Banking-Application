package com.example.WorldBankingApplication.payload.request;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "First name must not be blank") // these 2 annotations can be added to the other fields
    @Size(min = 2, max = 123, message = "First name must be at least two characters long")
    private String firstName;

    @NotBlank(message = "Last name must not be blank") // these 2 annotations can be added to the other fields
    @Size(min = 2, max = 123, message = "Last name must be at least two characters long")
    private  String lastName;

    private  String otherName;

    private String gender;

    @Pattern(regexp = "^(.+)@(.+)$", message = "Email is invalid")
    @NotBlank(message = "Email can not be empty")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Transient
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    private String address;

    private String stateOfOrigin;

    private String BVN;

    @NotBlank(message = "Phone number can not be blank")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private  String phoneNumber;

    private String pin;
}
