package com.example.WorldBankingApplication.repository;

import com.example.WorldBankingApplication.domain.entity.UserEntity;
import com.example.WorldBankingApplication.payload.request.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //this methods checks if the user already has an existing email in the database

    Boolean existsByEmail(String email);

  //  Optional<UserEntity> findByEmail(String username);
    Optional<UserEntity> findByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    UserEntity findByAccountNumber(String accountNumber);

}
