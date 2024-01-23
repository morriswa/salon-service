package org.morriswa.eecs447.utility;

import org.morriswa.eecs447.dao.UserProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; responsible for authenticating all web requests and locating appropriate user
 */

@Component @Profile("!test")
public class JdbcAuthenticationService implements UserDetailsService {

    private final UserProfileDao userProfileDao;

    @Autowired
    public JdbcAuthenticationService(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userProfileDao.findUser(username);
    }
}
