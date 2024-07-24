package com.example.WorldBankingApplication.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;
}
