package org.morriswa.eecs447.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class UserProfileDaoImpl implements UserProfileDao {
    private final Logger log;
    private final NamedParameterJdbcTemplate database;
    private final PasswordEncoder encoder;

    @Autowired
    public UserProfileDaoImpl(NamedParameterJdbcTemplate database, PasswordEncoder encoder) {
        this.log = LoggerFactory.getLogger(UserProfileDaoImpl.class);
        this.database = database;
        this.encoder = encoder;
    }

    @Override
    public User findUser(String username) {
        final var params = Map.of("username",username);

        return database.query("select * from user_profile where username=:username", params, rs->{

            if (rs.next())
                return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    Collections.emptyList());

            throw new UsernameNotFoundException(String.format("Could not locate user %s", username));
        });
    }

    public void register(String username, String password) {

        String encPassword = encoder.encode(password);

        final var params = Map.of("username", username, "password", encPassword);

        database.update("insert into user_profile (username, password) values (:username, :password)", params);
    }

    @Override
    public Long getUserId(String username) {
        final var query = """
            select user_id from user_profile where username=:username
        """;

        final var params = Map.of("username", username);

        return database.query(query, params, rs -> {
           if (rs.next()) {
               return rs.getLong("user_id");
           }
           throw new UsernameNotFoundException(String.format("Could not locate user %s", username));
        });
    }
}
