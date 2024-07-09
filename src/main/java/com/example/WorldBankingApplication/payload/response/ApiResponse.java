package com.example.WorldBankingApplication.payload.response;


import com.example.WorldBankingApplication.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
//this class will help us get the response data when the client login
public class ApiResponse<T> {

    private String message;

    private T data;

    private  String responseTime;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.responseTime = DateUtils.dateToString(LocalDateTime.now());
    }

    public ApiResponse(String responseTime) {
        this.responseTime = DateUtils.dateToString(LocalDateTime.now());
    }
}
