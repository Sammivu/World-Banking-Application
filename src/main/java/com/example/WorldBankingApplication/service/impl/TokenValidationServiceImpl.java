package com.example.WorldBankingApplication.service.impl;

import com.example.WorldBankingApplication.domain.entity.ConfirmationToken;
import com.example.WorldBankingApplication.domain.entity.UserEntity;
import com.example.WorldBankingApplication.repository.ConfirmationTokenRepository;
import com.example.WorldBankingApplication.repository.UserRepository;
import com.example.WorldBankingApplication.service.TokenValidationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenValidationServiceImpl implements TokenValidationService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Override
    public String validateToke(String token) {

        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository.findByToken(token);
        if (confirmationTokenOptional.isEmpty()) {
            return "Invalid token";
        }

        ConfirmationToken confirmationToken = confirmationTokenOptional.get();
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            return "Token has expired";
        }

        UserEntity userEntity = confirmationToken.getUserEntity();
        userEntity.setEnabled(true);
        userRepository.save(userEntity);

        //deleting the token once it has been used
        confirmationTokenRepository.delete(confirmationToken);
        return  "Email confirmed successfully";
    }

}
