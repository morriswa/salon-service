package org.morriswa.salon.dao;

import org.morriswa.salon.enumerated.AccountType;
import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.morriswa.salon.model.ContactInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AUTHOR: William A. Morris, Kevin Rivers, Makenna Loewenherz <br>
 * CREATION_DATE: 2024-01-22 <br>
 * PURPOSE: <br>
 * &emsp; provides an interface for interacting with the database to perform essential User actions
 */

@Component @SuppressWarnings("null")
public class UserProfileDaoImpl implements UserProfileDao {

    private final Logger log;
    private final NamedParameterJdbcTemplate database;
    private final PasswordEncoder encoder;

    @Autowired
    public UserProfileDaoImpl(NamedParameterJdbcTemplate database, PasswordEncoder encoder) {
        this.log = LoggerFactory.getLogger(getClass());
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
    public void register(String username, String password) throws Exception {
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
            if (error.endsWith("for key 'user_account.username'"))
                // throw a user-friendly error
                throw new ValidationException(
                    "username", true, username,
                    "There is already a user registered with that username! Try again :)");
            // if error was not expected, throw as is
            throw dpke;
        }
    }


    @Override
    public ContactInfo getContactInfo(Long userId) throws Exception {
        //Retrieving all columns from the contact_info table where user_id col is = to the param userId
        final var query = "select * from contact_info where user_id=:userId";
        //Mapping the named param to the Java var
        final var params = Map.of("userId",userId);

        Optional<ContactInfo> retrievedRecord = database.query(query, params, resultSet ->{
            //Checking if the record exists
            if(resultSet.next()) {
                //If it does, return the Contact Info
                return Optional.of(new ContactInfo(
                    //Grabbing cols as a string
                    resultSet.getString("first_name"), 
                    resultSet.getString("last_name"), 
                    resultSet.getString("phone_num"), 
                    resultSet.getString("email"), 
                    resultSet.getString("addr_one"), 
                    resultSet.getString("addr_two"), 
                    resultSet.getString("city"), 
                    resultSet.getString("state_code"), 
                    resultSet.getString("zip_code"), 
                    ContactPreference.getEnum(resultSet.getString("contact_pref")).description));
            }

            return Optional.empty(); //If it doesn't exist, return empty object
        });
        //If the retrievedRecord exists, return it, otherwise throw error
        return retrievedRecord.orElseThrow(()->new BadRequestException("You have not entered your contact information. Please update your information."));
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
            // extract database error message
            final var error = exception.getMostSpecificCause().getMessage();
            // if error was caused by duplicate username on user_profile table...
            if (error.endsWith("for key 'user_account.username'"))
                // throw a user-friendly error
                throw new ValidationException(
                        "username", true, newUsername,
                        "There is already a user registered with that username! Try again :)");
            // if error was not expected, throw as is
            throw exception;
        }
    }

    @Override
    public void createUserContactInfo(Long userId, ContactInfo request) throws Exception {

        final var query = """
            INSERT INTO contact_info
            (user_id, first_name, last_name, phone_num, email, addr_one, addr_two, city, state_code, zip_code, contact_pref)
            VALUES 
            (:UserId, :FirstName, :LastName, :PhoneNum, :Email, :AddrOne, :AddrTwo, :City, :StateCode, :ZipCode, :ContactPref) 
            """;

        final var params = new HashMap<String,Object>(){{
            put("UserId", userId);
            put("FirstName", request.firstName());
            put("LastName", request.lastName());
            put("PhoneNum", request.phoneNumber());
            put("Email", request.email());
            put("AddrOne", request.addressLineOne());
            put("AddrTwo", request.addressLineTwo());
            put("City", request.city());
            put("StateCode", request.stateCode());
            put("ZipCode", request.zipCode());
            put("ContactPref", ContactPreference.valueOf(request.contactPreference()).code);
        }};
        
        try {
            database.update(query, params);
        } catch (DuplicateKeyException dpke) {
            // extract database error message
            final var error = dpke.getMostSpecificCause().getMessage();
            // if error was caused by duplicate primary key on contact_info table...
            if (error.endsWith("for key 'contact_info.PRIMARY'"))
                // throw a user-friendly error
                throw new BadRequestException("You have already entered your contact info! Please use update endpoint if you are attempting to update your profile.");

            // if error was caused by duplicate phone number on contact_info table...
            if (error.endsWith("for key 'contact_info.phone_num'"))
                // throw a user-friendly error
                throw new ValidationException(
                        "phoneNumber", true, request.phoneNumber(),
                        "There is already a user registered with requested phone number!");

            // if error was caused by duplicate email on contact_info table...
            if (error.endsWith("for key 'contact_info.email'"))
                // throw a user-friendly error
                throw new ValidationException(
                        "email", true, request.email(),
                        "There is already a user registered with requested email address!");

            // if error was not expected, throw as is
            throw dpke;
        }
    }

    @Override
    public void updateUserContactInfo(Long userId, ContactInfo request) throws Exception {

        final var query = """
            UPDATE contact_info SET
                first_name = IFNULL(:firstName, first_name),
                last_name = IFNULL(:lastName, last_name),
                phone_num = IFNULL(:phoneNumber, phone_num),
                email = IFNULL(:email, email),
                addr_one = IFNULL(:addressLnOne, addr_one),
                addr_two = IFNULL(:addressLnTwo, addr_two),
                city = IFNULL(:city, city),
                state_code = IFNULL(:stateCode, state_code),
                zip_code = IFNULL(:zipCode, zip_code),
                contact_pref = IFNULL(:contactPreference, contact_pref)
            WHERE user_id = :userId
            """;

        final var params = new HashMap<String, Object>(){{
            put("userId", userId);
            put("firstName", request.firstName());
            put("lastName", request.lastName());
            put("phoneNumber", request.phoneNumber());
            put("email", request.email());
            put("addressLnOne", request.addressLineOne());
            put("addressLnTwo", request.addressLineTwo());
            put("city", request.city());
            put("stateCode", request.stateCode());
            put("zipCode", request.zipCode());
            put("contactPreference", request.contactPreference()==null?null:ContactPreference.valueOf(request.contactPreference()).code);
        }};

        try {
            database.update(query, params);
        } catch (DuplicateKeyException dpke) {
            // extract database error message
            final var error = dpke.getMostSpecificCause().getMessage();

            // if error was caused by duplicate phone number on contact_info table...
            if (error.endsWith("for key 'contact_info.phone_num'"))
            // throw a user-friendly error
                throw new ValidationException(
                        "phoneNumber", true, request.phoneNumber(),
                        "There is already a user registered with requested phone number!");

            // if error was caused by duplicate email on contact_info table...
            if (error.endsWith("for key 'contact_info.email'"))
                // throw a user-friendly error
                throw new ValidationException(
                        "email", true, request.email(),
                        "There is already a user registered with requested email address!");
            // if error was not expected, throw as is
            throw dpke;
        }
    }

    @Override
    public void promoteUser(Long promoterId, Long userId, AccountType role) {
        final var query = "update user_account set account_type = :accountCode, promoter = :promoterId where user_id = :userId";
        final var params = Map.of("accountCode", role.code, "promoterId", promoterId, "userId", userId);
        database.update(query, params);
    }

    @Override
    public void promoteUser(Long promoterId, String username, AccountType role) {
        final var query = "update user_account set account_type = :accountCode, promoter = :promoterId where username = :username";
        final var params = Map.of("accountCode", role.code, "promoterId", promoterId, "username", username);
        database.update(query, params);
    }

    @Override
    public void unlockClientPermissions(Long userId) {
        final var query = "update user_account set account_type = 'CLT' where user_id = :userId";
        final var params = Map.of("userId", userId);
        database.update(query, params);
    }
}
