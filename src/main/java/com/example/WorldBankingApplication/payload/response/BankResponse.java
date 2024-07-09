package com.example.WorldBankingApplication.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor

// Do not use @AllArgs here, it will cause problems
public class BankResponse {

    private String responseCode;

    private String responseMessage;

    private  AccountInfo accountInfo;

    public BankResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public BankResponse(String responseCode, String responseMessage, AccountInfo accountInfo) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.accountInfo = accountInfo;
    }
}
