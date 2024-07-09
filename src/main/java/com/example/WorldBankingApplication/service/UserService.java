package com.example.WorldBankingApplication.service;

import com.example.WorldBankingApplication.payload.request.CreditAndDebitRequest;
import com.example.WorldBankingApplication.payload.request.EnquiryRequest;
import com.example.WorldBankingApplication.payload.request.TransferRequest;
import com.example.WorldBankingApplication.payload.response.BankResponse;

public interface UserService {

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditAndDebitRequest creditAndDebitRequest);

    BankResponse debitAccount(CreditAndDebitRequest creditAndDebitRequest);

    BankResponse transfer(TransferRequest transferRequest);
}
