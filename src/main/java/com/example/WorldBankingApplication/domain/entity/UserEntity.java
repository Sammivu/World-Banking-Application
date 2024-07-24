package com.example.WorldBankingApplication.domain.entity;

import com.example.WorldBankingApplication.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name ="users_tbl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends BaseClass implements UserDetails {

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

    private boolean enabled = false;
//previous
//    @OneToMany(mappedBy = "userModel")
//    private List<JToken> jTokens;

    //after
    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<JToken> jTokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
