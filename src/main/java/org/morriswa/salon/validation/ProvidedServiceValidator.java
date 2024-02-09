package org.morriswa.salon.validation;

import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.ProvidedService;

public class ProvidedServiceValidator {
    public static void validateCreateProvidedServiceRequestOrThrow(ProvidedService createProvidedServiceRequest) throws ValidationException {

        ValidationException ve = new ValidationException();

        // cost rules
        if (createProvidedServiceRequest.defaultCost().scale() != 2) ve.addValidationError(
            "defaultCost",true, createProvidedServiceRequest.defaultCost().toString(),
            "Please enter a valid monetary value (two decimal places).");
        if (createProvidedServiceRequest.defaultCost().precision() > 5) ve.addValidationError(
            "defaultCost",true, createProvidedServiceRequest.defaultCost().toString(),
            "Service charges over $999.99 are not permitted.");
        if (createProvidedServiceRequest.defaultCost().doubleValue() <= 0.00
        || createProvidedServiceRequest.defaultCost().doubleValue() >= 1000) ve.addValidationError(
            "defaultCost",true, createProvidedServiceRequest.defaultCost().toString(),
            "Service charge must not be free or over $999.99");

        // service name rules
        if (!StrTools.hasValue(createProvidedServiceRequest.name())) ve.addValidationError(
            "name", true, createProvidedServiceRequest.name(),
            "All provided services must have a name.");
        else if (createProvidedServiceRequest.name().length() > 128) ve.addValidationError(
            "name", true, createProvidedServiceRequest.name(),
            "Provided service name must be 128 characters or less.");

        if (ve.containsErrors()) throw ve;
    }
}
