package org.morriswa.eecs447.utility;

import java.time.ZonedDateTime;

import org.morriswa.eecs447.model.UserAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; a mock authentication service for use during unit testing
 */

@Service @Profile("test")
public class TestJdbcAuthenticationService implements UserDetailsService {

    @Value("${testing.userId}") 
    private Long testingUserId;

    @Value("${testing.username}") 
    private String testingUsername;

    @Value("${testing.password}")
    private String testingPassword;

    @Override
    public UserAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserAccount(testingUserId, testingUsername, testingPassword, ZonedDateTime.now().minusDays(3));
    }
}
