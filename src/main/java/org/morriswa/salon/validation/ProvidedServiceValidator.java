package org.morriswa.salon.validation;

import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.ProvidedService;

public class ProvidedServiceValidator {

    public static final String ERROR_INVALID_MONEY_VALUE = "Please enter a valid monetary value (two decimal places).";
    public static final String ERROR_BIG_MONEY_VALUE = "Service charges over $999.99 are not permitted.";
    public static final String ERROR_OUT_OF_RANGE_MONEY_VALUE = "Service charge must not be free or over $999.99";
    public static final String ERROR_NO_NAME_SERVICE = "All provided services must have a name.";
    public static final String ERROR_LONG_NAME_SERVICE = "Provided service name must be 128 characters or less.";
    public static final String ERROR_MISSING_MONEY_VALUE = "All services must have a default cost!";
    public static final String ERROR_MISSING_LENGTH = "All services must have a default timeslot allocation (ie 1 = 15 minutes, 2 = 30 minutes, etc.)";
    public static final String ERROR_OUT_OF_RANGE_LENGTH = "Length must be at least 1 (15 minutes) and no greater than 32 (8 hours).";


    public static void validateCreateProvidedServiceRequestOrThrow(ProvidedService createProvidedServiceRequest) throws ValidationException {

        ValidationException ve = new ValidationException();

        // cost rules
        if (createProvidedServiceRequest.defaultCost()==null) ve.addValidationError(
            "defaultCost", true, null, ERROR_MISSING_MONEY_VALUE);
        else {
            if (createProvidedServiceRequest.defaultCost().scale() > 2) ve.addValidationError(
                    "defaultCost", true, createProvidedServiceRequest.defaultCost().toString(),
                    ERROR_INVALID_MONEY_VALUE);
            if (
                    (createProvidedServiceRequest.defaultCost().precision()
                        -
                    createProvidedServiceRequest.defaultCost().scale())
                > 3) ve.addValidationError(
                    "defaultCost", true, createProvidedServiceRequest.defaultCost().toString(),
                    ERROR_BIG_MONEY_VALUE);
            if (createProvidedServiceRequest.defaultCost().doubleValue() <= 0.00
                    || createProvidedServiceRequest.defaultCost().doubleValue() >= 1000) ve.addValidationError(
                    "defaultCost", true, createProvidedServiceRequest.defaultCost().toString(),
                    ERROR_OUT_OF_RANGE_MONEY_VALUE);
        }

        // length rules
        if (createProvidedServiceRequest.defaultLength()==null) ve.addValidationError(
                "defaultLength", true, null, ERROR_MISSING_LENGTH);
        else if (   createProvidedServiceRequest.defaultLength() < 1
                 || createProvidedServiceRequest.defaultLength() > 32) ve.addValidationError(
                    "defaultLength", true, createProvidedServiceRequest.defaultLength().toString(),
                    ERROR_OUT_OF_RANGE_LENGTH);

        // service name rules
        if (!StrTools.hasValue(createProvidedServiceRequest.name())) ve.addValidationError(
            "name", true, createProvidedServiceRequest.name(),
                ERROR_NO_NAME_SERVICE);
        else if (createProvidedServiceRequest.name().length() > 128) ve.addValidationError(
            "name", true, createProvidedServiceRequest.name(),
                ERROR_LONG_NAME_SERVICE);

        if (ve.containsErrors()) throw ve;
    }
}
