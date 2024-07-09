package com.example.WorldBankingApplication.service.impl;

import com.example.WorldBankingApplication.domain.entity.ConfirmationToken;
import com.example.WorldBankingApplication.domain.entity.UserEntity;
import com.example.WorldBankingApplication.domain.enums.Role;
import com.example.WorldBankingApplication.exception.UserNotEnabledException;
import com.example.WorldBankingApplication.infrastructure.config.JwtService;
import com.example.WorldBankingApplication.payload.request.EmailDetails;
import com.example.WorldBankingApplication.payload.request.LoginRequest;
import com.example.WorldBankingApplication.payload.request.UserRequest;
import com.example.WorldBankingApplication.payload.response.AccountInfo;
import com.example.WorldBankingApplication.payload.response.ApiResponse;
import com.example.WorldBankingApplication.payload.response.BankResponse;
import com.example.WorldBankingApplication.payload.response.JwtAuthResponse;
import com.example.WorldBankingApplication.repository.ConfirmationTokenRepository;
import com.example.WorldBankingApplication.repository.UserRepository;
import com.example.WorldBankingApplication.service.AuthService;
import com.example.WorldBankingApplication.service.EmailService;
import com.example.WorldBankingApplication.utils.AccountUtils;

import com.example.WorldBankingApplication.utils.EmailTemplate;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private  final UserRepository userRepository;
    //this is for email
    private  final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final JwtService jwtService;
    //To encode the password
    private final PasswordEncoder passwordEncoder;

    @Value("${baseUrl}")
    private String baseUrl;

    @Override
    public BankResponse registerUser(UserRequest userRequest) throws MessagingException {

        if (userRepository.existsByEmail(userRequest.getEmail())){
            BankResponse response= BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
            .build();

            return response;
        }
        if(!userRequest.getPassword().equals(userRequest.getConfirmPassword())){
            BankResponse response = BankResponse.builder()
                    .responseCode("000")
                    .responseMessage("Passwords don not match")
                    .build();
            return response;
        }

        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode( userRequest.getPassword()))
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

        ConfirmationToken confirmationToken = new ConfirmationToken(savedUser);
        confirmationTokenRepository.save(confirmationToken);

        String confirmationUrl = EmailTemplate.getVerificationUrl(baseUrl, confirmationToken.getToken());

        // Add email alert here
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation!! Your account have been created Successfully.\nYour Account Details: \n"+
                        "Account Name : "+ savedUser.getFirstName()+ " "+ savedUser.getLastName()+" "+savedUser.getOtherName()+
                        "\nAccount Number : "+savedUser.getAccountNumber())
                .build();
        emailService.sendSimpleMailMessage (emailDetails, savedUser.getFirstName(), savedUser.getLastName(), confirmationUrl);

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
       // Optional<UserEntity> userEntityOptional= userRepository.findByEmail(loginRequest.getEmail());

        //Authenticate the user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword())
        );

        //it checks for the user (email) if not found throws
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User with the following email: "
                        +loginRequest.getEmail()+" not found"));

        //This prompts when email had not been verified
        if(!user.isEnabled()){
            throw new UserNotEnabledException("User Account not enabled, Please check you email to confirm your account");
        }



        //This prompts the user everytime there is a login
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("Your are logged in")
                .recipient(loginRequest.getEmail())
                .messageBody("You Logged into your account. If you did not initiate this, contact support desk...")
                .build();
        emailService.sendEmailAlert(loginAlert);


        //UserEntity user = userEntityOptional.get();
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                "Login Successfully",
                                JwtAuthResponse.builder()
                                        .accessToken(jwtService.generateToken(user))
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
