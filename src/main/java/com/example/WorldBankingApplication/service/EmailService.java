package com.example.WorldBankingApplication.service;

import com.example.WorldBankingApplication.payload.request.EmailDetails;

public interface EmailService {

    void  sendEmailAlert(EmailDetails emailDetails);

}
