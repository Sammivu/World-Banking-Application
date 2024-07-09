package com.example.WorldBankingApplication.service.impl;

import com.example.WorldBankingApplication.domain.entity.UserEntity;
import com.example.WorldBankingApplication.domain.enums.Role;
import com.example.WorldBankingApplication.payload.request.EmailDetails;
import com.example.WorldBankingApplication.payload.request.LoginRequest;
import com.example.WorldBankingApplication.payload.request.UserRequest;
import com.example.WorldBankingApplication.payload.response.AccountInfo;
import com.example.WorldBankingApplication.payload.response.ApiResponse;
import com.example.WorldBankingApplication.payload.response.BankResponse;
import com.example.WorldBankingApplication.payload.response.JwtAuthResponse;
import com.example.WorldBankingApplication.repository.UserRepository;
import com.example.WorldBankingApplication.service.AuthService;
import com.example.WorldBankingApplication.service.EmailService;
import com.example.WorldBankingApplication.utils.AccountUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private  final UserRepository userRepository;

    //this is for email
    private  final EmailService emailService;

    @Override
    public BankResponse registerUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())){
            BankResponse response= BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
            .build();

            return response;
        }

        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .gender(userRequest.getGender())
                .phoneNumber(userRequest.getPhoneNumber())
                .address(userRequest.getAddress())
                .BVN(userRequest.getBVN())
                .pin(userRequest.getPin())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .bankName("World Bank Limited")
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .profilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png")
                .role(Role.USER)
                .build();
        UserEntity savedUser = userRepository.save(newUser);

        // Add email alert here
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation!! Your account have been created Successfully.\nYour Account Details: \n"+
                        "Account Name : "+ savedUser.getFirstName()+ " "+ savedUser.getLastName()+" "+savedUser.getOtherName()+
                        "\nAccount Number : "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .bankName(savedUser.getBankName())
                        .accountName(savedUser.getFirstName()+ " "+ savedUser.getLastName()+ " "+savedUser.getOtherName())
                .build())
        .build();
    }

    @Override
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(LoginRequest loginRequest) {
        Optional<UserEntity> userEntityOptional= userRepository.findByEmail(loginRequest.getEmail());


        //This prompts the user everytime there is a login
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("Your are logged in")
                .recipient(loginRequest.getEmail())
                .messageBody("You Logged into your account. If you did not initiate this, contact support desk...")
                .build();
        emailService.sendEmailAlert(loginAlert);


        UserEntity user = userEntityOptional.get();
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                "Login Successfully",
                                JwtAuthResponse.builder()
                                        .accessToken("generate token here")
                                        .tokenType("Bearer")
                                        .id(user.getId())
                                        .email(user.getEmail())
                                        .gender(user.getGender())
                                        .firstName(user.getFirstName())
                                        .lastName(user.getLastName())
                                        .profilePicture(user.getProfilePicture())
                                .build()
                        )
                );

    }
}
