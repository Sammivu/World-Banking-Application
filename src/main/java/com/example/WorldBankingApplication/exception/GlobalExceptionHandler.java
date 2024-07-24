package com.example.WorldBankingApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<String>handleUserNotEnabledException(UserNotEnabledException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
    public ResponseEntity<String>handleNotFoundException(NotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
