package com.example.WorldBankingApplication.domain.entity;

import com.example.WorldBankingApplication.domain.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jtoken_tbl")
public class JToken extends BaseClass {

    @Column(unique = true)
    public String token;

   // private String tokenType;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    //previous
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    public UserEntity userModel;

    //after
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}