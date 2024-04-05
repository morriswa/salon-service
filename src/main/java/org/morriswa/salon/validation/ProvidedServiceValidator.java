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
        if (createProvidedServiceRequest.getCost()==null) ve.addValidationError(
            "defaultCost", true, null, ERROR_MISSING_MONEY_VALUE);
        else {
            if (createProvidedServiceRequest.getCost().scale() > 2) ve.addValidationError(
                    "defaultCost", true, createProvidedServiceRequest.getCost().toString(),
                    ERROR_INVALID_MONEY_VALUE);
            if (
                    (createProvidedServiceRequest.getCost().precision()
                        -
                    createProvidedServiceRequest.getCost().scale())
                > 3) ve.addValidationError(
                    "defaultCost", true, createProvidedServiceRequest.getCost().toString(),
                    ERROR_BIG_MONEY_VALUE);
            if (createProvidedServiceRequest.getCost().doubleValue() <= 0.00
                    || createProvidedServiceRequest.getCost().doubleValue() >= 1000) ve.addValidationError(
                    "defaultCost", true, createProvidedServiceRequest.getCost().toString(),
                    ERROR_OUT_OF_RANGE_MONEY_VALUE);
        }

        // length rules
        if (createProvidedServiceRequest.getLength()==null) ve.addValidationError(
                "defaultLength", true, null, ERROR_MISSING_LENGTH);
        else if (   createProvidedServiceRequest.getLength() < 1
                 || createProvidedServiceRequest.getLength() > 32) ve.addValidationError(
                    "defaultLength", true, createProvidedServiceRequest.getLength().toString(),
                    ERROR_OUT_OF_RANGE_LENGTH);

        // service name rules
        if (!StrTools.hasValue(createProvidedServiceRequest.getName())) ve.addValidationError(
            "name", true, createProvidedServiceRequest.getName(),
                ERROR_NO_NAME_SERVICE);
        else if (createProvidedServiceRequest.getName().length() > 128) ve.addValidationError(
            "name", true, createProvidedServiceRequest.getName(),
                ERROR_LONG_NAME_SERVICE);

        if (ve.containsErrors()) throw ve;
    }

    public static void validateUpdateOrThrow(ProvidedService request) throws ValidationException {
        ValidationException ve = new ValidationException();

        // cost rules
        if (request.getCost()!=null) {
            if (request.getCost().scale() > 2) ve.addValidationError(
                    "cost", true, request.getCost().toString(),
                    ERROR_INVALID_MONEY_VALUE);
            if (
                    (request.getCost().precision()
                            -
                            request.getCost().scale())
                            > 3) ve.addValidationError(
                    "cost", true, request.getCost().toString(),
                    ERROR_BIG_MONEY_VALUE);
            if (request.getCost().doubleValue() <= 0.00
                    || request.getCost().doubleValue() >= 1000) ve.addValidationError(
                    "cost", true, request.getCost().toString(),
                    ERROR_OUT_OF_RANGE_MONEY_VALUE);
        }

        // length rules
        if (request.getLength()!=null &&
                (request.getLength() < 1
                || request.getLength() > 32)) ve.addValidationError(
                "defaultLength", true, request.getLength().toString(),
                ERROR_OUT_OF_RANGE_LENGTH);

        // service name rules
        if (StrTools.isNotNullButBlank(request.getName())) ve.addValidationError(
                "name", false, request.getName(),
                ERROR_NO_NAME_SERVICE);
        else if (
                request.getName()!=null
                    &&
                request.getName().length() > 128
        ) ve.addValidationError(
                "name", true, request.getName(),
                ERROR_LONG_NAME_SERVICE);

        if (ve.containsErrors()) throw ve;
    }
}
