package org.morriswa.salon.dao;

import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.enumerated.Pronouns;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.ContactInfo;
import org.morriswa.salon.model.EmployeeInfo;
import org.morriswa.salon.model.UserAccount;
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
import java.util.HashMap;
import java.util.HashSet;
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
                    Pronouns.getPronounStr(resultSet.getString("pronouns")),
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
    public void updateUserContactInfo(Long userId, ContactInfo request) throws Exception {

        final var query = """
            UPDATE contact_info SET
                first_name = IFNULL(:firstName, first_name),
                last_name = IFNULL(:lastName, last_name),
                pronouns = IFNULL(:pronouns, pronouns),
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
            put("pronouns", request.pronouns());
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
    public EmployeeInfo getEmployeeInfo(Long employeeId) throws BadRequestException {

        //Retrieving all columns from the contact_info table where user_id col is = to the param userId
        final var query = """
            select *
            from employee emp
            left join contact_info ci on emp.employee_id = ci.user_id
            where emp.employee_id=:employeeId""";
        //Mapping the named param to the Java var
        final var params = Map.of("employeeId",employeeId);

        Optional<EmployeeInfo> retrievedRecord = database.query(query, params, resultSet ->{
            //Checking if the record exists
            if(resultSet.next()) {


                //If it does, return the Contact Info
                return Optional.of(new EmployeeInfo(
                        //Grabbing cols as a string
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        Pronouns.getPronounStr(resultSet.getString("pronouns")),
                        resultSet.getString("phone_num"),
                        resultSet.getString("email"),
                        resultSet.getString("addr_one"),
                        resultSet.getString("addr_two"),
                        resultSet.getString("city"),
                        resultSet.getString("state_code"),
                        resultSet.getString("zip_code"),
                        ContactPreference.getEnum(resultSet.getString("contact_pref")).description,
                        resultSet.getString("bio")));
            }

            return Optional.empty(); //If it doesn't exist, return empty object
        });
        //If the retrievedRecord exists, return it, otherwise throw error
        return retrievedRecord.orElseThrow(()->new BadRequestException("Could not find an employee profile!"));
    }

    @Override
    public void updateEmployeeProfile(Long userId, EmployeeInfo request) throws ValidationException {
        final var query = """
            
            UPDATE contact_info SET
                first_name = IFNULL(:firstName, first_name),
                last_name = IFNULL(:lastName, last_name),
                pronouns = IFNULL(:pronouns, pronouns),
                phone_num = IFNULL(:phoneNumber, phone_num),
                email = IFNULL(:email, email),
                addr_one = IFNULL(:addressLnOne, addr_one),
                addr_two = IFNULL(:addressLnTwo, addr_two),
                city = IFNULL(:city, city),
                state_code = IFNULL(:stateCode, state_code),
                zip_code = IFNULL(:zipCode, zip_code),
                contact_pref = IFNULL(:contactPreference, contact_pref)
                WHERE user_id = :userId;
            
            UPDATE employee SET
                bio = IFNULL(:bio, bio)
                WHERE employee_id = :userId;
            
     
        """;

        final var params = new HashMap<String, Object>(){{
            put("userId", userId);
            put("firstName", request.firstName());
            put("lastName", request.lastName());
            put("pronouns", request.pronouns());
            put("phoneNumber", request.phoneNumber());
            put("email", request.email());
            put("addressLnOne", request.addressLineOne());
            put("addressLnTwo", request.addressLineTwo());
            put("city", request.city());
            put("stateCode", request.stateCode());
            put("zipCode", request.zipCode());
            put("contactPreference",
                    request.contactPreference()
                            ==null?null:ContactPreference.valueOf(request.contactPreference()).code);
            put("bio", request.bio());
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
}
