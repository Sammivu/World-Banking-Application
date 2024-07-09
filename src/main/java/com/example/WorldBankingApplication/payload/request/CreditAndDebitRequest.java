package com.example.WorldBankingApplication.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditAndDebitRequest {

    private String accountNumber;

//    private String firstName;
//
//    private String lastName;

    private BigDecimal amount;



}
