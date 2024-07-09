package com.example.WorldBankingApplication.service;

import com.example.WorldBankingApplication.payload.request.EmailDetails;
import jakarta.mail.MessagingException;

public interface EmailService {

    void  sendEmailAlert(EmailDetails emailDetails);

    void sendSimpleMailMessage(EmailDetails message, String firstName, String lastName, String link) throws MessagingException;

}
