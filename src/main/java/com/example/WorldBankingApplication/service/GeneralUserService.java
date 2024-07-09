package com.example.WorldBankingApplication.service;

import com.example.WorldBankingApplication.payload.response.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralUserService {

    ResponseEntity<BankResponse<String>> uploadProfilePicture(Long id, MultipartFile multipartFile);
}
