package org.morriswa.eecs447.dao;

import org.morriswa.eecs447.exception.BadRequestException;
import org.morriswa.eecs447.model.UserAccount;
import org.morriswa.eecs447.model.ContactInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.time.ZoneId;
import java.util.Map;

@Component @SuppressWarnings("null")
public class UserProfileDaoImpl implements UserProfileDao {
    private final NamedParameterJdbcTemplate database;
    private final PasswordEncoder encoder;

    @Autowired
    public UserProfileDaoImpl(NamedParameterJdbcTemplate database, PasswordEncoder encoder) {
        this.database = database;
        this.encoder = encoder;
    }

    @Override
    public UserAccount findUser(String username) {
        // defn query, inject params, query database and return the result
        final var query = "select * from user_account where username=:username";
        final var params = Map.of("username",username);
        return database.query(query, params, rs->{
            // check that a database record exists
            if (rs.next())
                // and return the requested user, formatted for compatibility with Spring Security Filter
                return new UserAccount(
                    // retrieve column "user_id" from result set as Long
                    rs.getLong("user_id"),
                    // retrieve column "username" from result set as String
                    rs.getString("username"),
                    // retrieve column "password" from result set as String
                    rs.getString("password"),
                    // retrieve column "date_created" from result set as Timestamp
                    rs.getTimestamp("date_created")
                        // and cast to Zoned Date Time at System Date.
                        .toLocalDateTime().atZone(ZoneId.systemDefault()),
                    rs.getString("account_type"));
            // if a record is not found, throw an exception. This will trigger a 401 Http Response.
            throw new UsernameNotFoundException(String.format("Could not locate user %s", username));
        });
    }

    @Override
    public void register(String username, String password) throws BadRequestException {
        // database should always store an encrypted password
        final String encPassword = encoder.encode(password);
        // defn query, inject params
        final var query = "insert into user_account (username, password) values (:username, :password)";
        final var params = Map.of("username", username, "password", encPassword);
        try { // attempt to update the database
            database.update(query, params);
        } catch (DuplicateKeyException dpke) { // if a duplicate key exception is thrown...
            // extract database error message
            final var error = dpke.getMostSpecificCause().getMessage();
            // if error was caused by duplicate username on user_profile table...
            if (error.endsWith("for key 'user_profile.username'"))
                // throw a user-friendly error
                throw new BadRequestException("There is already a user registered with that username! Try again :)");
            // if error was not expected, throw as is
            throw dpke;
        }
    }


    @Override
    public ContactInfo getContactInfo(Long userId) {
        return null;
    }

    @Override
    public void updateUserPassword(Long userId, String currentPassword, String newPassword) throws Exception {

    }

    @Override
    public void changeUsername(Long userId, String newUsername) throws Exception {

    }

    @Override
    public void createUserContactInfo(Long userId, ContactInfo request) {

    }

    @Override
    public void updateUserContactInfo(Long userId, ContactInfo request) {

    }
}
