package com.example.WorldBankingApplication.payload.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Email can not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password can not be empty")
    private  String password;
}
