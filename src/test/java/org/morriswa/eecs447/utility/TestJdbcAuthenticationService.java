package org.morriswa.eecs447.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;


/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; responsible for authenticating all web requests and locating appropriate user
 */

@Component @Profile("test")
public class TestJdbcAuthenticationService implements UserDetailsService {

    @Value("${testing.password}")
    private String testingPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, testingPassword, Collections.emptyList());
    }
}
