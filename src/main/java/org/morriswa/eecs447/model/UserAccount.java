package org.morriswa.eecs447.model;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
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
    
    public UserAccount(Long userId, String username, String encodedPassword, ZonedDateTime dateCreated) {
        this.userId = userId;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.dateCreated = dateCreated;
    }

    public Long getUserId() {
        return userId;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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