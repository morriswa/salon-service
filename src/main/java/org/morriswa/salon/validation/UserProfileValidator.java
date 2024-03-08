package org.morriswa.salon.validation;

import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.ClientInfo;
import org.morriswa.salon.model.UserInfo;

import java.util.Set;

/**
 * provides important methods to validate various request fields throughout the application
 *
 * @author William A. Morris
 * @since 2024-01-23
 */
public class UserProfileValidator {

    public static final Set<String> validPronouns = Set.of("H", "S", "T");

    // add static validation methods to be used throughout the application here

    public static void validateUsernameOrThrow(String requestedUsername) throws Exception {
        // add username validation guards here

        if (!StrTools.hasValue(requestedUsername))
            throw new ValidationException("username", true, requestedUsername, "Username field must not be blank!");

        var errors = new ValidationException();
        if (requestedUsername.length() > 64)
            errors.addValidationError("username", true, requestedUsername,
                "Username must not be longer than 64 characters!");
        else if (requestedUsername.length() < 4)
            errors.addValidationError("username", true, requestedUsername,
                "Username must be longer than 4 characters!");

        if (!requestedUsername.matches("^[A-Za-z0-9-_.]*$"))
            errors.addValidationError("username", true, requestedUsername,"""
                Username may only contain alphanumeric characters, \
                underscores (_), hyphens (-), and periods (.)""");

        if (errors.containsErrors()) throw errors;
    }

    public static void validatePasswordOrThrow(String password) throws Exception {
        validatePasswordOrThrow(password, "password");
    }

    private static void validatePasswordOrThrow(String password, String field) throws Exception {
        // add password validation guards here

        if (!StrTools.hasValue(password))
            throw new ValidationException(field, true, "********",
                    "Password must not be blank!");

        if (password.length() < 8)
            throw new ValidationException(field, true, "********",
                    "Password must be longer than 8 characters!");
    }

    public static void validatePasswordChangeOrThrow(String password, String confirmPassword) throws Exception {
        // add password validation guards here

        validatePasswordOrThrow(password, "password");
        validatePasswordOrThrow(confirmPassword, "confirmPassword");

        if (!password.equals(confirmPassword))
            throw new ValidationException("confirmPassword", true, "********",
                    "Fields 'password' and 'confirmPassword' must be matching!");
    }

    public static void validateCreateProfileRequestOrThrow(UserInfo createProfileRequest) throws ValidationException {
        var error = new ValidationException();
        
        // validate required fields

        // pronoun validation rules
        final var pronouns = createProfileRequest.getPronouns();
        if (!StrTools.hasValue(pronouns)) error.addValidationError(
                "pronouns", true, pronouns, "Please select your preferred pronouns!");
        else if (!validPronouns.contains(pronouns)) error.addValidationError(
                "pronouns", true, pronouns, "Valid pronoun codes are 'H' for him, 'S' for her, 'T' for them...");

        // first name validation rules
        final var firstName = createProfileRequest.getFirstName();
        if (!StrTools.hasValue(firstName)) error.addValidationError(
            "firstName", true, firstName, "First name must not be blank!");
        else if (firstName.length() > 32) error.addValidationError(
            "firstName", true, firstName, "First name must be 32 characters or less!");

        // last name validation rules
        final var lastName = createProfileRequest.getLastName();
        if (!StrTools.hasValue(lastName)) error.addValidationError(
            "lastName", true, lastName, "Last name must not be blank!");
        else if (lastName.length() > 32) error.addValidationError(
            "lastName", true, lastName, "Last name must be 32 characters or less!");
        
        // phone number validation rules
        final var phoneNumber = createProfileRequest.getPhoneNumber();
        if (!StrTools.hasValue(phoneNumber)) error.addValidationError(
            "phoneNumber", true, phoneNumber, "Phone number must not be blank!");
        else {
            if (phoneNumber.length() != 10) error.addValidationError(
            "phoneNumber", true, phoneNumber, "Phone number MUST be 10 digits!");
            
            if (!phoneNumber.matches("^[0-9]*$")) error.addValidationError(
                "phoneNumber", true, phoneNumber, "Phone number must only contain numeric characters!");
        }
        
        // email validation rules
        final var email = createProfileRequest.getEmail();
        if (!StrTools.hasValue(email)) error.addValidationError(
            "email", true, email, "Email must not be blank!");
        else if (email.length() > 100) error.addValidationError(
            "email", true, email, "Email must not be longer than 100 characters!");
            
        // address validation rules
        final var addressLnOne = createProfileRequest.getAddressLineOne();
        final var addressLnTwo = createProfileRequest.getAddressLineTwo();
        final var city = createProfileRequest.getCity();
        final var stateCode = createProfileRequest.getStateCode();
        final var zipCode = createProfileRequest.getZipCode();
        if (!StrTools.hasValue(addressLnOne)) error.addValidationError(
            "addressLineOne", true, addressLnOne, "Address must not be blank!");
        else if (addressLnOne.length()>50) error.addValidationError(
                "addressLineOne", true, addressLnOne, "Address fields must be shorter than 50 characters!");
            
        if (StrTools.hasValue(addressLnTwo) && addressLnTwo.length()>50) error.addValidationError(
                    "addressLineTwo", true, addressLnTwo, "Address fields must be shorter than 50 characters!");
        
        if (!StrTools.hasValue(city)) error.addValidationError(
            "city", true, city, "City must not be blank!");
        else if (city.length()>50) error.addValidationError(
                "city", true, city, "City name must be shorter than 50 characters!");
                  
        if (!StrTools.hasValue(stateCode)) error.addValidationError(
            "stateCode", true, stateCode, "State must not be blank!");
        else {
            if (stateCode.length() != 2) error.addValidationError(
                "stateCode", true, stateCode, "State code MUST be 2 characters!");
            
            if (!stateCode.matches("^[A-Z]*$")) error.addValidationError(
                "stateCode", true, stateCode, "State code must contain 2 uppercase characters!");
        } 

        if (!StrTools.hasValue(zipCode)) error.addValidationError(
            "zipCode", true, zipCode, "Zip code must not be blank!");
        else if (zipCode.length() !=5
                || !zipCode.matches("^[0-9]*$")
        ) error.addValidationError(
            "zipCode", true, zipCode, "Zip code MUST be 5 DIGITS!");

        // contact preferences validation rules
        final var contactPref = createProfileRequest.getContactPreference();
        if (!StrTools.hasValue(contactPref)) error.addValidationError(
            "contactPreference", true, contactPref, "Contact preference must not be blank!");
        else try { ContactPreference.valueOf(contactPref); }
            catch (Exception e) {
                error.addValidationError(
                "contactPreference", true, contactPref,
                "Invalid option for Contact Preference, must be 'Email', 'PhoneCall', 'TextMessage'...");
            }
        

        if (error.containsErrors()) throw error;
    }

    public static void validateUpdateUserProfileRequestOrThrow(ClientInfo updateProfileRequest) throws ValidationException {
        var error = new ValidationException();
        
        // validate required fields

        // pronoun validation rules
        if (StrTools.hasValue(updateProfileRequest.getPronouns()) && !validPronouns.contains(updateProfileRequest.getPronouns())) error.addValidationError(
            "pronouns", true, updateProfileRequest.getPronouns(), "Valid pronoun codes are 'H' for him, 'S' for her, 'T' for them...");

        // first name validation rules
        if(StrTools.isNotNullButBlank(updateProfileRequest.getFirstName())) error.addValidationError(
            "firstName", true, updateProfileRequest.getFirstName(), "First name must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.getFirstName()) && updateProfileRequest.getFirstName().length() > 32) error.addValidationError(
            "firstName", true, updateProfileRequest.getFirstName(), "First name must be 32 characters or less!");

        // last name validation rules
        if(StrTools.isNotNullButBlank(updateProfileRequest.getLastName())) error.addValidationError(
            "lastName", true, updateProfileRequest.getLastName(), "Last name must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.getLastName())&& updateProfileRequest.getLastName().length() > 32) error.addValidationError(
            "lastName", true, updateProfileRequest.getLastName(), "Last name must be 32 characters or less!");
        
        // phone number validation rules
        if (StrTools.hasValue(updateProfileRequest.getPhoneNumber())) {
            if (updateProfileRequest.getPhoneNumber().length() != 10) error.addValidationError(
                "phoneNumber", true, updateProfileRequest.getPhoneNumber(), "Phone number MUST be 10 digits!");

            if (!updateProfileRequest.getPhoneNumber().matches("^[0-9]*$")) error.addValidationError(
                "phoneNumber", true, updateProfileRequest.getPhoneNumber(), "Phone number must only contain numeric characters!");
        }
       

        // email validation rules
        if(StrTools.isNotNullButBlank(updateProfileRequest.getEmail())) error.addValidationError(
            "email", true, updateProfileRequest.getEmail(), "Email address must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.getEmail()) && updateProfileRequest.getEmail().length() > 100) error.addValidationError(
            "email", true, updateProfileRequest.getEmail(), "Email must not be longer than 100 characters!");
            
        // address validation rules
        if(StrTools.isNotNullButBlank(updateProfileRequest.getAddressLineOne())) error.addValidationError(
            "addressLineOne", true, updateProfileRequest.getAddressLineOne(), "Address must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.getAddressLineOne()) && updateProfileRequest.getAddressLineOne().length()>50) error.addValidationError(
            "addressLineOne", true, updateProfileRequest.getAddressLineOne(), "Address fields must be shorter than 50 characters!");
            
        if (StrTools.hasValue(updateProfileRequest.getAddressLineTwo()) && updateProfileRequest.getAddressLineTwo().length()>50) error.addValidationError(
                    "addressLineTwo", true, updateProfileRequest.getAddressLineTwo(), "Address fields must be shorter than 50 characters!");
        
        if(StrTools.isNotNullButBlank(updateProfileRequest.getCity())) error.addValidationError(
            "city", true, updateProfileRequest.getCity(), "City must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.getCity()) && updateProfileRequest.getCity().length()>50) error.addValidationError(
                "city", true, updateProfileRequest.getCity(), "City name must be shorter than 50 characters!");
                  
        if (StrTools.hasValue(updateProfileRequest.getStateCode())) {
            if (updateProfileRequest.getStateCode().length() != 2) error.addValidationError(
                "stateCode", true, updateProfileRequest.getStateCode(), "State code MUST be 2 characters!");
            if (!updateProfileRequest.getStateCode().matches("^[A-Z]*$")) error.addValidationError(
                "stateCode", true, updateProfileRequest.getStateCode(), "State code must contain 2 uppercase characters!");
        }

        if (StrTools.hasValue(updateProfileRequest.getZipCode()) && (
                updateProfileRequest.getZipCode().length() != 5
                || !updateProfileRequest.getZipCode().matches("^[0-9]*$")
        )) error.addValidationError(
            "zipCode", true, updateProfileRequest.getZipCode(), "Zip code MUST be 5 DIGITS!");

        // contact preferences validation rules
        if (StrTools.hasValue(updateProfileRequest.getContactPreference()))
            try { ContactPreference.valueOf(updateProfileRequest.getContactPreference()); }
            catch (Exception e) {
                error.addValidationError(
                "contactPreference", true, updateProfileRequest.getContactPreference(),
                "Invalid option for Contact Preference, must be 'Email', 'PhoneCall', 'TextMessage'...");
            }
        

        if (error.containsErrors()) throw error;
    }
}
