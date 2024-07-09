package com.example.WorldBankingApplication.exception;

public class UserNotEnabledException extends RuntimeException{

    public UserNotEnabledException (String message){
        super(message);
    }
}
