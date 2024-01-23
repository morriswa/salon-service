package org.morriswa.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.morriswa.model.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserProfileDaoImpl implements UserProfileDao {
    private final NamedParameterJdbcTemplate database;
    private final PasswordEncoder passwordEncoder;

    @Autowired public UserProfileDaoImpl(NamedParameterJdbcTemplate database, PasswordEncoder passwordEncoder){
        this.database = database;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(String username, String password) {
        final var query = "INSERT INTO user_profile(username, password) VALUES(:username, :password)";
        final var encryptedPassword = passwordEncoder.encode(password);
        final var params = Map.of("username",username, "password",encryptedPassword);
        database.update(query, params);
    }

    @Override
    public User authenticateUser(String username) {
        final var query = "SELECT username, password FROM user_profile WHERE username=:username";
        final var params = Map.of("username", username);
        return database.query(query, params, resultset->{
            if(resultset.next()){
                return new User(resultset.getString("username"),
                    resultset.getString("password"),
                    Collections.emptyList());
            }
            
            throw new UsernameNotFoundException("Could not find user.");
        });
    }

    @Override
    public UserProfileResponse getUserProfile(String username) {
        final var query = "SELECT username, user_id FROM user_profile WHERE username=:username";
        final var params = Map.of("username", username);
        return database.query(query, params, resultset->{
            if(resultset.next()){
                return new UserProfileResponse(resultset.getLong("user_id"),
                resultset.getString("username"));
            }
            
            throw new UsernameNotFoundException("Could not find user.");
        });
    }
    
}
