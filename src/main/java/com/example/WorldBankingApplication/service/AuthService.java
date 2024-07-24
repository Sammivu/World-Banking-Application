package com.example.WorldBankingApplication.service;

import com.example.WorldBankingApplication.payload.request.LoginRequest;
import com.example.WorldBankingApplication.payload.request.PasswordResetConfirmationRequest;
import com.example.WorldBankingApplication.payload.request.PasswordResetRequest;
import com.example.WorldBankingApplication.payload.request.UserRequest;
import com.example.WorldBankingApplication.payload.response.ApiResponse;
import com.example.WorldBankingApplication.payload.response.BankResponse;
import com.example.WorldBankingApplication.payload.response.JwtAuthResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    BankResponse registerUser(UserRequest userRequest) throws MessagingException;
    ResponseEntity<ApiResponse<JwtAuthResponse>>loginUser(LoginRequest loginRequest);

    String forgotPasswordRequest(PasswordResetRequest passwordResetRequest)throws MessagingException;
    String confirmResetPassword(String token, PasswordResetConfirmationRequest passwordResetConfirmationRequest);
}
