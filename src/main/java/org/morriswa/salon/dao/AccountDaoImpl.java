package org.morriswa.salon.dao;

import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.UserAccount;
import org.morriswa.salon.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.*;

/**
 * AUTHOR: William A. Morris, Kevin Rivers, Makenna Loewenherz <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for interacting with the database to perform essential User Account actions
 */

@Component @SuppressWarnings("null")
public class AccountDaoImpl implements AccountDao {

    private final Logger log;
    private final NamedParameterJdbcTemplate database;
    private final PasswordEncoder encoder;

    @Autowired
    public AccountDaoImpl(NamedParameterJdbcTemplate database, PasswordEncoder encoder) {
        this.log = LoggerFactory.getLogger(getClass());
        this.database = database;
        this.encoder = encoder;
    }

    @Override
    public UserAccount findUser(String username) {
        // defn query, inject params, query database and return the result
        final var query = """
            select
                    uac.user_id,
                    uac.username,
                    uac.password,
                    uac.date_created,
                        IFNULL((select 1 from contact_info where user_id=uac.user_id), 0)
                    as registered_user,
                        IFNULL((select 1 from employee where employee_id=uac.user_id), 0)
                    as registered_employee,
                        IFNULL((select 1 from client where client_id=uac.user_id), 0)
                    as registered_client
                from user_account uac where username=:username""";

        final var params = Map.of("username",username);
        return database.query(query, params, rs->{
            // check that a database record exists
            if (rs.next()) {

                final boolean isRegistered = rs.getLong("registered_user")==1;
                final boolean isClient = rs.getLong("registered_client")==1;
                final boolean isEmployee = rs.getLong("registered_employee")==1;

                var perms = new HashSet<SimpleGrantedAuthority>();

                // if user has neither client nor employee privileges
                // they are considered a new user
                if (!(isClient||isEmployee))
                    perms.add(new SimpleGrantedAuthority("NUSER"));

                // if user is registered in contact info table, they are considered a user
                if (isRegistered) perms.add(new SimpleGrantedAuthority("USER"));

                // if user is registered in employee table, they are considered an employee
                if (isEmployee) perms.add(new SimpleGrantedAuthority("EMPLOYEE"));

                // if user is registered in client table, they are considered a client

                if (isClient) perms.add(new SimpleGrantedAuthority("CLIENT"));

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
                    perms);
            }
            // if a record is not found, throw an exception. This will trigger a 401 Http Response.
            throw new UsernameNotFoundException(String.format("Could not locate user %s", username));
        });
    }

    private void expectDuplicateError(
            String table,
            String column,
            DuplicateKeyException error,
            String field,
            String rejectedValue,
            String msgOnReject
    ) throws ValidationException {

        var errorMsg = error.getMostSpecificCause().getMessage().toLowerCase();

        if (    errorMsg.contains(table)
            &&  errorMsg.contains(column)
            &&  errorMsg.contains(rejectedValue))
            // throw a user-friendly error
            throw new ValidationException(field, true, rejectedValue, msgOnReject);
    }

    @Override
    public void register(String username, String password) throws Exception {
        // database should always store an encrypted password
        final String encPassword = encoder.encode(password);
        // defn query, inject params
        final var query = "insert into user_account (username, password) values (:username, :password)";
        final var params = Map.of("username", username, "password", encPassword);
        try { // attempt to update the database
            database.update(query, params);
        } catch (DuplicateKeyException dpke) { // if a duplicate key exception is thrown...
            expectDuplicateError(
                "user_account", "username", dpke, "username", username,
                "There is already a user registered with that username! Try again :)");
            throw dpke;
        }
    }

    @Override
    public void updateUserPassword(Long userId, String currentEncodedPassword, String currentPassword, String newPassword) throws Exception {
        //Checking to see if the current password coming from the form matches the encoded password in our table
        if(!encoder.matches(currentPassword, currentEncodedPassword)){
            throw new BadRequestException("The password you entered is incorrect. Try again.");
            //Exception that is sent if it isn't.
        }

        //Encode the newPassword set by the user
        final var newEncodedPassword = encoder.encode(newPassword);
        //Query the user_account table to set the password to the newEncodedPassword by userID
        final var query = "UPDATE user_account SET password = :newPassword WHERE user_id = :userId";
        //Mapping
        final var params = Map.of("userId",userId, "newPassword",newEncodedPassword); 

        //Update the database with new encoded password
        database.update(query, params);
    }

    @Override
    public void changeUsername(Long userId, String newUsername) throws Exception {
        final var query = "UPDATE user_account SET username = :newUsername WHERE user_id = :userId";
        final var params = Map.of("newUsername", newUsername, "userId", userId);
        try {
            database.update(query, params);
        }
        catch (DuplicateKeyException exception) {
            expectDuplicateError(
                "user_account", "username", exception, "username", newUsername,
                "There is already a user registered with that username! Try again :)");
        }
    }

    @Override
    public void enterContactInfo(Long userId, UserInfo request) throws Exception {

        final var query = """
            INSERT INTO contact_info
            (user_id, first_name, last_name, pronouns, phone_num, email, addr_one, addr_two, city, state_code, zip_code, contact_pref)
            VALUES
            (:UserId, :FirstName, :LastName, :Pronouns, :PhoneNum, :Email, :AddrOne, :AddrTwo, :City, :StateCode, :ZipCode, :ContactPref) 
            """;

        final var params = new HashMap<String,Object>(){{
            put("UserId", userId);
            put("FirstName", request.getFirstName());
            put("LastName", request.getLastName());
            put("Pronouns", request.getPronouns());
            put("PhoneNum", request.getPhoneNumber());
            put("Email", request.getEmail());
            put("AddrOne", request.getAddressLineOne());
            put("AddrTwo", request.getAddressLineTwo());
            put("City", request.getCity());
            put("StateCode", request.getStateCode());
            put("ZipCode", request.getZipCode());
            put("ContactPref", ContactPreference.valueOf(request.getContactPreference()).code);
        }};
        
        try {
            database.update(query, params);
        } catch (DuplicateKeyException dpke) {
            // extract database error message
            expectDuplicateError(
                    "contact_info", "phone_num", dpke, "phoneNumber", request.getPhoneNumber(),
                    "There is already a user registered with requested phone number!");
            expectDuplicateError(
                    "contact_info", "email", dpke, "email", request.getEmail(),
                    "There is already a user registered with requested email address!");

            // if error was caused by duplicate primary key on contact_info table...
            final var error = dpke.getMostSpecificCause().getMessage().toLowerCase();
            if (    error.contains("contact_info")
                    &&      error.contains("primary key")
                    &&      error.contains("user_id"))
                // throw a user-friendly error
                throw new BadRequestException("You have already entered your contact info! Please use update endpoint if you are attempting to update your profile.");

            // if error was not expected, throw as is
            throw dpke;
        }
    }

    @Override
    public void completeClientRegistration(Long userId) {
        final var query = "insert into client (client_id) values (:userId)";
        final var params = Map.of("userId", userId);
        database.update(query, params);
    }

    @Override
    public void completeEmployeeRegistration(Long userId) {
        final var query = "insert into employee (employee_id) values (:userId)";
        final var params = Map.of("userId", userId);
        database.update(query, params);
    }

}
