package org.morriswa.salon.model;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Set;

import org.morriswa.salon.enumerated.AccountType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom implementation of Spring Security's User Details interface

 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-30 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for interacting with the database to perform essential User actions
 */
public class UserAccount implements UserDetails {

    private final Long userId;
    private final String username;
    private final String encodedPassword;
    private final ZonedDateTime dateCreated;
    private final AccountType role;

    public UserAccount(Long userId, String username, String encodedPassword, ZonedDateTime dateCreated, String role) {
        this.userId = userId;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.dateCreated = dateCreated;
        this.role = AccountType.getEnum(role);
    }

    public Long getUserId() {
        return userId;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (this.role) {
            case Admin:
                return Set.of(
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("EMPLOYEE"),
                    new SimpleGrantedAuthority("CLIENT"),
                    new SimpleGrantedAuthority("USER"));
            case Employee:
                return Set.of(
                    new SimpleGrantedAuthority("EMPLOYEE"),
                    new SimpleGrantedAuthority("CLIENT"),
                    new SimpleGrantedAuthority("USER"));
            case Client:
                return Set.of(
                    new SimpleGrantedAuthority("CLIENT"),
                    new SimpleGrantedAuthority("USER"));
            default:
                return Set.of(new SimpleGrantedAuthority("USER"));
        }
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
    
}