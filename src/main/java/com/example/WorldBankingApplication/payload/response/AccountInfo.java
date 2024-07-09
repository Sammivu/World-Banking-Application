package com.example.WorldBankingApplication.payload.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {

    private  String accountName;

    private BigDecimal accountBalance; //we used BigDecimal here because were dealing with currency and we need precision.

    private  String accountNumber;

    private  String bankName;
}
