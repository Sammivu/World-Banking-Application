package com.example.WorldBankingApplication.infrastructure.controller;

import com.example.WorldBankingApplication.payload.request.CreditAndDebitRequest;
import com.example.WorldBankingApplication.payload.request.EnquiryRequest;
import com.example.WorldBankingApplication.payload.request.TransferRequest;
import com.example.WorldBankingApplication.payload.response.BankResponse;
import com.example.WorldBankingApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/balance-enquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit-account")
    public BankResponse creditAccount(@RequestBody CreditAndDebitRequest creditAndDebitRequest){

        return userService.creditAccount(creditAndDebitRequest);
    }

    @PostMapping("/debit-account")
    public BankResponse debitAccount(@RequestBody CreditAndDebitRequest creditAndDebitRequest){
        return userService.debitAccount(creditAndDebitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest){

        return userService.transfer(transferRequest);
    }
}
