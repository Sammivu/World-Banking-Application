package com.example.WorldBankingApplication.infrastructure.controller;

import com.example.WorldBankingApplication.payload.request.CreditAndDebitRequest;
import com.example.WorldBankingApplication.payload.request.EnquiryRequest;
import com.example.WorldBankingApplication.payload.request.TransferRequest;
import com.example.WorldBankingApplication.payload.response.BankResponse;
import com.example.WorldBankingApplication.service.AuthService;
import com.example.WorldBankingApplication.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/balance-enquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit-account")
    public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditAndDebitRequest creditAndDebitRequest){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return ResponseEntity.ok(userService.creditAccount(creditAndDebitRequest, email));
    }

    @PostMapping("/debit-account")
    public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditAndDebitRequest creditAndDebitRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return ResponseEntity.ok(userService.debitAccount(creditAndDebitRequest, email));
    }

    @PostMapping("/transfer")
    public ResponseEntity<BankResponse> transfer(@RequestBody TransferRequest transferRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return ResponseEntity.ok(userService.transfer(transferRequest, email));
    }
}
