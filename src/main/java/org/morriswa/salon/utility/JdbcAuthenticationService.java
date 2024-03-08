package org.morriswa.salon.utility;

import org.morriswa.salon.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; responsible for locating appropriate user object
 * for authentication with spring security filter
 */

@Service @Profile("!test")
public class JdbcAuthenticationService implements UserDetailsService {

    private final AccountDao accounts;

    @Autowired
    public JdbcAuthenticationService(AccountDao accounts){
        this.accounts = accounts;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return accounts.findUser(username);
    }
}
