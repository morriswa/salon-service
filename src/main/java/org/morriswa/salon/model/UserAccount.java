package org.morriswa.salon.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

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
    private final AccountPermissions permissions;

    public UserAccount(Long userId, String username, String encodedPassword, ZonedDateTime dateCreated, AccountPermissions permissions) {
        this.userId = userId;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.dateCreated = dateCreated;
        this.permissions = permissions;
    }

    public Long getUserId() {
        return userId;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = new HashSet<GrantedAuthority>(){{ add(new SimpleGrantedAuthority("USER")); }};

        if (permissions.client()) authorities.add(new SimpleGrantedAuthority("CLIENT"));

        if (permissions.employee()) authorities.add(new SimpleGrantedAuthority("EMPLOYEE"));

        if (permissions.admin()) authorities.add(new SimpleGrantedAuthority("ADMIN"));

        return authorities;
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
