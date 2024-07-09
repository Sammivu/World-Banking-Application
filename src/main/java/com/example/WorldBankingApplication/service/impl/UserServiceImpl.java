package com.example.WorldBankingApplication.service.impl;

import com.example.WorldBankingApplication.domain.entity.UserEntity;
import com.example.WorldBankingApplication.payload.request.CreditAndDebitRequest;
import com.example.WorldBankingApplication.payload.request.EmailDetails;
import com.example.WorldBankingApplication.payload.request.EnquiryRequest;
import com.example.WorldBankingApplication.payload.request.TransferRequest;
import com.example.WorldBankingApplication.payload.response.AccountInfo;
import com.example.WorldBankingApplication.payload.response.BankResponse;
import com.example.WorldBankingApplication.repository.UserRepository;
import com.example.WorldBankingApplication.service.EmailService;
import com.example.WorldBankingApplication.service.UserService;
import com.example.WorldBankingApplication.utils.AccountUtils;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.example.WorldBankingApplication.utils.AccountUtils.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        //checking if account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        //if the account does not exist
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE) // because it is static, we can also use it like this
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }

        //if the user exist
        UserEntity foundUserAccount = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NUMBER_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NUMBER_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUserAccount.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUserAccount.getFirstName() + " " + foundUserAccount.getLastName())
                        .build())
                .build();
    }


    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {

        //checking if account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        //if the account does not exist
        if (!isAccountExists) {
            return ACCOUNT_NUMBER_NON_EXISTS_MESSAGE;
        }

        //if the user exist
        UserEntity foundUserAccount = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUserAccount.getFirstName() + " " + foundUserAccount.getOtherName() + " " + foundUserAccount.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditAndDebitRequest creditAndDebitRequest) {
        //checking if the account exist
        boolean isAccountExists = userRepository.existsByAccountNumber(creditAndDebitRequest.getAccountNumber());

        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }
        //this is to deposit to an account
        UserEntity userToCredit = userRepository.findByAccountNumber(creditAndDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditAndDebitRequest.getAmount()));
        //  userToCredit.setFirstName(creditAndDebitRequest.getFirstName());
        //  userToCredit.setLastName(creditAndDebitRequest.getLastName());
        userRepository.save(userToCredit);


        //send email alert
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(userToCredit.getEmail())
                .messageBody("Your account has been credited with " +
                        creditAndDebitRequest.getAmount() + " from " +
                        userToCredit.getFirstName() + " " + userToCredit.getLastName() +
                        "Your account balance is " + userToCredit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();
    }


    @Override
    public BankResponse debitAccount(CreditAndDebitRequest creditAndDebitRequest) {

        boolean isAccountExists = userRepository.existsByAccountNumber(creditAndDebitRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .build();
        }

        UserEntity userToDebit = userRepository.findByAccountNumber(creditAndDebitRequest.getAccountNumber());

        //Checking for insufficient balance
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditAndDebitRequest.getAmount().toBigInteger();

        if (availableBalance.intValue() < debitAmount.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_BALANCE_INSUFFICIENT_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_BALANCE_INSUFFICIENT_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance()
                    .subtract(creditAndDebitRequest.getAmount()));

            userRepository.save(userToDebit);


            //Debit alert here
            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of " + creditAndDebitRequest.getAmount() + " has been debited from your account!" +
                            "Your account balance is " + userToDebit.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(debitAlert);


            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(userToDebit.getAccountNumber())
                            .build())
                    .build();


        }
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {

        /*
         1. FIRST CHECK IF THE DESTINATION ACCOUNT NUMBER EXISTS
         2. THEN CHECK IF AMOUNT TO SEND IS AVAILABLE
         3. THEN DEDUCT THE AMOUNT TO SEND FROM SENDER BALANCE
         4. THEN ADD THE SENT AMOUNT TO THE RECIEVER BALANCE
         5. THEN SEND A DEBIT ALERT AND A CREDIT ALERT TO BOTH
           */

        boolean isDestinationAccountExists= userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());

        if(!isDestinationAccountExists){
            return BankResponse.builder()
                    .responseCode("008")
                    .responseMessage("Account number does not exists")
                    .build();
        }

        UserEntity sourceAccountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        //checks the account balance is greater then zero, read from right to left
        if(transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance())> 0){
            return BankResponse.builder()
                    .responseCode("009")
                    .responseMessage("INSUFFICIENT BALANCE")
                    .accountInfo(null)
                    .build();
        }

        //debiting the account
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));

        userRepository.save(sourceAccountUser);
        String sourceUsername = sourceAccountUser.getFirstName()+" "
                +sourceAccountUser.getOtherName()+" "+sourceAccountUser.getLastName();


        //Sending Alert
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The Sum of "
                        + transferRequest.getAmount()
                        +" has been debited from your account. Your current balance is"
                        +sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert); //send alert

        // to credit the destination Account number
        UserEntity destinationAccountUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));

        userRepository.save(destinationAccountUser);

        //Sending Credit alert
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("Your account has been credited with "+ transferRequest.getAmount()+ " from "+ sourceUsername
                    +"your current account balance iis "+destinationAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);


        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("TRANSFER SUCCESSFUL")
                .accountInfo(null)
                .build();
    }
}
