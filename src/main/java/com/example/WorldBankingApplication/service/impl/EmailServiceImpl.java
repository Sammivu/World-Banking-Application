package com.example.WorldBankingApplication.service.impl;

import com.example.WorldBankingApplication.payload.request.EmailDetails;
import com.example.WorldBankingApplication.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    //We are getting the value of our host email this is what we are using to snd to the user
    @Value("${spring.mail.username}")
    private  String senderEmail; // senderEmail is the email we use to send the email to others


    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);
        }catch (MailException e){
            throw new RuntimeException(e);
        }

    }
}
