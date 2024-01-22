package org.morriswa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final NamedParameterJdbcTemplate jdbc;
    private final PasswordEncoder encoder;
    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    public UserDetailsServiceImpl(NamedParameterJdbcTemplate jdbc, PasswordEncoder enc) {
        this.jdbc = jdbc;
        this.encoder = enc;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final var params = Map.of("username",username);

        log.info("Attempting to locate user {}", username);

        return jdbc.query("select * from user_profile where username=:username", params, rs->{

            if (rs.next()) {
                return new User(rs.getString("username"),rs.getString("password"), Collections.emptyList());
            }

            throw new UsernameNotFoundException(String.format("Could not locate user %s", username));
        });
    }

    public void register(String username, String password) {

        String encPassword = encoder.encode(password);

        final var params = Map.of("username", username, "password", encPassword);

        log.info("Attempting to register user {}", username);

        jdbc.update("insert into user_profile (username, password) values (:username, :password)", params);
    }
}
