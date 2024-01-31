package org.morriswa.eecs447.validation;

import org.morriswa.eecs447.enumerated.AccountType;
import org.morriswa.eecs447.exception.BadRequestException;
import org.morriswa.eecs447.exception.ValidationException;
import org.morriswa.eecs447.model.AccountRequest;

import java.util.ArrayList;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-23 <br>
 * PURPOSE: <br>
 * &emsp; provides important methods to validate various request fields throughout the application
 */

public class ServiceValidator {

    // add static validation methods to be used throughout the application here

    public static void validateUsernameOrThrow(String requestedUsername) throws Exception {
        // add username validation guards here

        if (requestedUsername==null||requestedUsername.isBlank()||requestedUsername.isEmpty())
            throw new ValidationException("username", true, requestedUsername, "Username field must not be blank!");

        var errors = new ArrayList<ValidationException.ValidationError>();
        if (requestedUsername.length() > 64)
            errors.add(new ValidationException.ValidationError("username", true, requestedUsername,
                    "Username must not be longer than 64 characters!"));
        else if (requestedUsername.length() < 4)
            errors.add(new ValidationException.ValidationError("username", true, requestedUsername,
                    "Username must be longer than 4 characters!"));

        if (!requestedUsername.matches("^[A-Za-z0-9-_.]*$"))
            errors.add(new ValidationException.ValidationError("username", true, requestedUsername,
                    """
                        Username may only contain alphanumeric characters, \
                        underscores (_), hyphens (-), and periods (.)"""));

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }

    public static void validatePasswordOrThrow(String password) throws Exception {
        validatePasswordOrThrow(password, "password");
    }

    private static void validatePasswordOrThrow(String password, String field) throws Exception {
        // add password validation guards here

        if (password==null||password.isBlank()||password.isEmpty())
            throw new ValidationException(field, true, "********",
                    "Field must not be blank!");

        if (password.length() < 8)
            throw new ValidationException(field, true, "********",
                    "Field must be longer than 8 characters!");
    }

    public static void validatePasswordChangeOrThrow(String password, String confirmPassword) throws Exception {
        // add password validation guards here

        validatePasswordOrThrow(password, "password");
        validatePasswordOrThrow(confirmPassword, "confirmPassword");

        if (!password.equals(confirmPassword))
            throw new ValidationException("confirmPassword", true, "********",
                    "Fields password and confirmPassword must be matching!");
    }

    public static void validatePromoteRequestOrThrow(AccountRequest request) throws ValidationException, BadRequestException {
        
        if (request.role()==null||request.role().isBlank()||request.role().isEmpty())
            throw new ValidationException("role", true, request.role(), "Field must not be blank!");

        if (AccountType.getEnum(request.role())==null) throw new BadRequestException("Invalid account type code! Must be 'USR', 'CLT', 'EMP', 'ADM'...");

        boolean usernameIsMissing = false;
        if (request.username()==null||request.username().isBlank()||request.username().isEmpty()) usernameIsMissing = true;

        boolean userIdIsMissing = request.userId()==null;

        if (userIdIsMissing && usernameIsMissing)
            throw new BadRequestException("Must include a userId or username in request!");
    }
}
