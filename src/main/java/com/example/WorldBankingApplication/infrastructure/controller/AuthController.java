package com.example.WorldBankingApplication.infrastructure.controller;

import com.example.WorldBankingApplication.payload.request.LoginRequest;
import com.example.WorldBankingApplication.payload.request.PasswordResetConfirmationRequest;
import com.example.WorldBankingApplication.payload.request.PasswordResetRequest;
import com.example.WorldBankingApplication.payload.request.UserRequest;
import com.example.WorldBankingApplication.payload.response.ApiResponse;
import com.example.WorldBankingApplication.payload.response.BankResponse;
import com.example.WorldBankingApplication.payload.response.JwtAuthResponse;
import com.example.WorldBankingApplication.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-user")
    public BankResponse createUserAccount(@Valid @RequestBody UserRequest userRequest) throws MessagingException {

        return  authService.registerUser(userRequest);
    }
    @PostMapping("/login-user")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest){

        return authService.loginUser(loginRequest);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPasswordRequest(@Valid @RequestBody PasswordResetRequest passwordResetRequest) throws MessagingException {

        return ResponseEntity.ok(authService.forgotPasswordRequest(passwordResetRequest));
    }

    //Do this after the email message
    @GetMapping("/confirm-forgot-password")
    public ResponseEntity<?> confirmForgotPassword(@RequestParam("token") String token){
        return ResponseEntity.ok("Token confirmed");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> confirmPasswordReset(@RequestParam("token") String token, @Valid @RequestBody PasswordResetConfirmationRequest resetConfirmationRequest){

        return ResponseEntity.ok(authService.confirmResetPassword(token,resetConfirmationRequest));
    }
}
