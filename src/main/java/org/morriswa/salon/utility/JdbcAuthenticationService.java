package org.morriswa.salon.utility;

import org.morriswa.salon.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * responsible for locating appropriate user object for authentication with spring security filter
 *
 * @author William A. Morris
 * @since 2024-01-22
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
