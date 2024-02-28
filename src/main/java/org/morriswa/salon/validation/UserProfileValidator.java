package org.morriswa.salon.validation;

import org.morriswa.salon.enumerated.ContactPreference;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.ContactInfo;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-23 <br>
 * PURPOSE: <br>
 * &emsp; provides important methods to validate various request fields throughout the application
 */

public class UserProfileValidator {

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

    public static void validateCreateProfileRequestOrThrow(ContactInfo createProfileRequest) throws ValidationException {
        var error = new ValidationException();
        
        // validate required fields

        // first name validation rules
        if (!StrTools.hasValue(createProfileRequest.firstName())) error.addValidationError(
            "firstName", true, createProfileRequest.firstName(), "First name must not be blank!");
        else if (createProfileRequest.firstName().length() > 32) error.addValidationError(
            "firstName", true, createProfileRequest.firstName(), "First name must be 32 characters or less!");

        // last name validation rules
        if (!StrTools.hasValue(createProfileRequest.lastName())) error.addValidationError(
            "lastName", true, createProfileRequest.lastName(), "Last name must not be blank!");
        else if (createProfileRequest.lastName().length() > 32) error.addValidationError(
            "lastName", true, createProfileRequest.lastName(), "Last name must be 32 characters or less!");
        
        // phone number validation rules
        if (!StrTools.hasValue(createProfileRequest.phoneNumber())) error.addValidationError(
            "phoneNumber", true, createProfileRequest.phoneNumber(), "Phone number must not be blank!");
        else {
            if (createProfileRequest.phoneNumber().length() != 10) error.addValidationError(
            "phoneNumber", true, createProfileRequest.phoneNumber(), "Phone number MUST be 10 digits!");
            
            if (!createProfileRequest.phoneNumber().matches("^[0-9]*$")) error.addValidationError(
                "phoneNumber", true, createProfileRequest.phoneNumber(), "Phone number must only contain numeric characters!");
        }
        
        // email validation rules
        if (!StrTools.hasValue(createProfileRequest.email())) error.addValidationError(
            "email", true, createProfileRequest.email(), "Email must not be blank!");
        else if (createProfileRequest.email().length() > 100) error.addValidationError(
            "email", true, createProfileRequest.email(), "Email must not be longer than 100 characters!"); 
            
        // address validation rules
        if (!StrTools.hasValue(createProfileRequest.addressLineOne())) error.addValidationError(
            "addressLineOne", true, createProfileRequest.addressLineOne(), "Address must not be blank!");
        else if (createProfileRequest.addressLineOne().length()>50) error.addValidationError(
                "addressLineOne", true, createProfileRequest.addressLineOne(), "Address fields must be shorter than 50 characters!"); 
            
        if (StrTools.hasValue(createProfileRequest.addressLineTwo()) && createProfileRequest.addressLineTwo().length()>50) error.addValidationError(
                    "addressLineTwo", true, createProfileRequest.addressLineTwo(), "Address fields must be shorter than 50 characters!"); 
        
        if (!StrTools.hasValue(createProfileRequest.city())) error.addValidationError(
            "city", true, createProfileRequest.city(), "City must not be blank!");
        else if (createProfileRequest.city().length()>50) error.addValidationError(
                "city", true, createProfileRequest.city(), "City name must be shorter than 50 characters!"); 
                  
        if (!StrTools.hasValue(createProfileRequest.stateCode())) error.addValidationError(
            "stateCode", true, createProfileRequest.stateCode(), "State must not be blank!");
        else {
            if (createProfileRequest.stateCode().length() != 2) error.addValidationError(
                "stateCode", true, createProfileRequest.stateCode(), "State code MUST be 2 characters!");
            
            if (!createProfileRequest.stateCode().matches("^[A-Z]*$")) error.addValidationError(
                "stateCode", true, createProfileRequest.stateCode(), "State code must contain 2 uppercase characters!");
        } 

        if (!StrTools.hasValue(createProfileRequest.zipCode())) error.addValidationError(
            "zipCode", true, createProfileRequest.zipCode(), "Zip code must not be blank!");
        else if (createProfileRequest.zipCode().length() !=5
                || !createProfileRequest.zipCode().matches("^[0-9]*$")
        ) error.addValidationError(
            "zipCode", true, createProfileRequest.zipCode(), "Zip code MUST be 5 DIGITS!");

        // contact preferences validation rules
        if (!StrTools.hasValue(createProfileRequest.contactPreference())) error.addValidationError(
            "contactPreference", true, createProfileRequest.contactPreference(), "Contact preference must not be blank!");
        else try { ContactPreference.valueOf(createProfileRequest.contactPreference()); } 
            catch (Exception e) {
                error.addValidationError(
                "contactPreference", true, createProfileRequest.contactPreference().toString(), 
                "Invalid option for Contact Preference, must be 'Email', 'PhoneCall', 'TextMessage'...");
            }
        

        if (error.containsErrors()) throw error;
    }

    public static void validateUpdateUserProfileRequestOrThrow(ContactInfo updateProfileRequest) throws ValidationException {
        var error = new ValidationException();
        
        // validate required fields

        // first name validation rules
        if(StrTools.isNotNullButBlank(updateProfileRequest.firstName())) error.addValidationError(
            "firstName", true, updateProfileRequest.firstName(), "First name must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.firstName()) && updateProfileRequest.firstName().length() > 32) error.addValidationError(
            "firstName", true, updateProfileRequest.firstName(), "First name must be 32 characters or less!");

        // last name validation rules
        if(StrTools.isNotNullButBlank(updateProfileRequest.lastName())) error.addValidationError(
            "lastName", true, updateProfileRequest.lastName(), "Last name must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.lastName())&& updateProfileRequest.lastName().length() > 32) error.addValidationError(
            "lastName", true, updateProfileRequest.lastName(), "Last name must be 32 characters or less!");
        
        // phone number validation rules
        if (StrTools.hasValue(updateProfileRequest.phoneNumber())) {
            if (updateProfileRequest.phoneNumber().length() != 10) error.addValidationError(
                "phoneNumber", true, updateProfileRequest.phoneNumber(), "Phone number MUST be 10 digits!");

            if (!updateProfileRequest.phoneNumber().matches("^[0-9]*$")) error.addValidationError(
                "phoneNumber", true, updateProfileRequest.phoneNumber(), "Phone number must only contain numeric characters!");
        }
       

        // email validation rules
        if(StrTools.isNotNullButBlank(updateProfileRequest.email())) error.addValidationError(
            "email", true, updateProfileRequest.email(), "Email address must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.email()) && updateProfileRequest.email().length() > 100) error.addValidationError(
            "email", true, updateProfileRequest.email(), "Email must not be longer than 100 characters!"); 
            
        // address validation rules
        if(StrTools.isNotNullButBlank(updateProfileRequest.addressLineOne())) error.addValidationError(
            "addressLineOne", true, updateProfileRequest.addressLineOne(), "Address must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.addressLineOne()) && updateProfileRequest.addressLineOne().length()>50) error.addValidationError(
            "addressLineOne", true, updateProfileRequest.addressLineOne(), "Address fields must be shorter than 50 characters!"); 
            
        if (StrTools.hasValue(updateProfileRequest.addressLineTwo()) && updateProfileRequest.addressLineTwo().length()>50) error.addValidationError(
                    "addressLineTwo", true, updateProfileRequest.addressLineTwo(), "Address fields must be shorter than 50 characters!"); 
        
        if(StrTools.isNotNullButBlank(updateProfileRequest.city())) error.addValidationError(
            "city", true, updateProfileRequest.city(), "City must not be blank!");
        if (StrTools.hasValue(updateProfileRequest.city()) && updateProfileRequest.city().length()>50) error.addValidationError(
                "city", true, updateProfileRequest.city(), "City name must be shorter than 50 characters!"); 
                  
        if (StrTools.hasValue(updateProfileRequest.stateCode())) {
            if (updateProfileRequest.stateCode().length() != 2) error.addValidationError(
                "stateCode", true, updateProfileRequest.stateCode(), "State code MUST be 2 characters!");
            if (!updateProfileRequest.stateCode().matches("^[A-Z]*$")) error.addValidationError(
                "stateCode", true, updateProfileRequest.stateCode(), "State code must contain 2 uppercase characters!");
        }

        if (StrTools.hasValue(updateProfileRequest.zipCode()) && (
                updateProfileRequest.zipCode().length() != 5
                || !updateProfileRequest.zipCode().matches("^[0-9]*$")
        )) error.addValidationError(
            "zipCode", true, updateProfileRequest.zipCode(), "Zip code MUST be 5 DIGITS!");

        // contact preferences validation rules
        if (StrTools.hasValue(updateProfileRequest.contactPreference()))
            try { ContactPreference.valueOf(updateProfileRequest.contactPreference()); } 
            catch (Exception e) {
                error.addValidationError(
                "contactPreference", true, updateProfileRequest.contactPreference().toString(), 
                "Invalid option for Contact Preference, must be 'Email', 'PhoneCall', 'TextMessage'...");
            }
        

        if (error.containsErrors()) throw error;
    }
}
