package org.morriswa.utility;

import org.morriswa.dao.UserProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
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
