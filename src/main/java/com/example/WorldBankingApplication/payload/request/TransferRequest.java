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
//This class allows for local transfer
public class TransferRequest {

    private String destinationAccountNumber;

    private  String sourceAccountNumber;

    private BigDecimal amount;
}
