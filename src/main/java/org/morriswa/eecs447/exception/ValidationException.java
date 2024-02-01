package org.morriswa.eecs447.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AUTHOR: William A. Morris <br>
 * CREATION_DATE: 2024-01-23 <br>
 * PURPOSE: <br>
 * &emsp; Exception to throw when the server cannot complete a request due to user input error
 */
public class ValidationException extends Exception {

    /**
     * Creates an individual Validation Error to be thrown as part of Validation Exception
     *
     * @param field name of the request field that failed validation
     * @param required whether the field being validated is required or optional
     * @param rejectedValue value passed in request
     * @param message error message to be provided
     */
    public record ValidationError (
            String field,
            boolean required,
            String rejectedValue,
            String message
    ) { }

    private final List<ValidationError> validationErrors;

    /**
     * Creates a Validation Exception when only one error needs to be returned
     *
     * @param problemField name of the request field that failed validation
     * @param required whether the field being validated is required or optional
     * @param problemValue value passed in request
     * @param errorMessage error message to be provided
     */
    public ValidationException(String problemField, boolean required, String problemValue, String errorMessage) {
        super();
        this.validationErrors = Collections.singletonList(new ValidationError(
                problemField,
                required,
                problemValue,
                errorMessage
        ));
    }

    /**
     * Creates a Validation Exception when multiple errors need to be returned
     *
     * @param validationErrors to be included in response
     */
    public ValidationException(List<ValidationError> validationErrors) {
        super();
        this.validationErrors = validationErrors;
    }

    /**
     * Creates a blank Validation Exception that users can build upon
     *
     * @param validationErrors to be included in response
     */
    public ValidationException() {
        super();
        this.validationErrors = new ArrayList<ValidationError>();
    }

    public List<ValidationError> getValidationErrors() {
        return this.validationErrors;
    }

     /**
     * Adds an error to an existing validation exception
     *
     * @param problemField name of the request field that failed validation
     * @param required whether the field being validated is required or optional
     * @param problemValue value passed in request
     * @param errorMessage error message to be provided
     */
    public void addValidationError(String problemField, boolean required, String problemValue, String errorMessage) {
        this.validationErrors.add(new ValidationError(problemField, required, problemValue, errorMessage));
    }

    public boolean containsErrors() {
        return !this.validationErrors.isEmpty();
    }
}