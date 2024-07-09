package com.example.WorldBankingApplication.domain.entity;

import com.example.WorldBankingApplication.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name ="users_tbl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends BaseClass{

    private String firstName;

    private String lastName;

    private String otherName;

    private String email;

    private String password;

    private String gender;

    private String stateOfOrigin;

    private String address;

    private String phoneNumber;

    private String BVN;

    private String pin;

    private String accountNumber;

    private BigDecimal accountBalance;

    private String bankName;

    private String profilePicture;

    private String status;

    private Role role;
}
