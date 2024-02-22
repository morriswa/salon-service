package org.morriswa.salon.validation;

import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.AppointmentRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.time.ZoneOffset.UTC;

public class ScheduleRequestValidator {

    public static void validateRescheduleAppointmentRequest(AppointmentRequest request) throws ValidationException {

        ValidationException ve = new ValidationException();

        if (request.timeZone() == null) {
            ve.addValidationError(
                    "timeZone", true, null,
                    "Must provide a time zone for all scheduling requests.");
            throw ve;
        }

        if (request.time() == null) ve.addValidationError(
                "time", true, null,
                "Must provide new appointment time.");
        else if (request.time().isBefore(Instant.now().atZone(UTC))) ve.addValidationError(
                "time", true, request.time().toString(),
                "Appointments can not take place in the past!");

        if (request.length() == null) ve.addValidationError(
                "length", true, null,
                "Must provide new appointment length (in 15 minute increments... ie 1 = 15 minutes, 2 = 30 minutes, etc.");
        else if (request.length() < 1 || request.length() > 32) ve.addValidationError(
                "length", true, request.length().toString(),
                "Length must be in 15 minute increments, at least 1 (15 minutes) and no more than 32 (8 hours)");

        if (ve.containsErrors()) throw ve;

    }

    public static void validateBookAppointmentRequest(AppointmentRequest request) throws ValidationException {
        ValidationException ve = new ValidationException();

        if (request.serviceId() == null) ve.addValidationError(
                "serviceId", true, null,
                "Must provide type of appointment via Service ID.");

        if (request.employeeId() == null) ve.addValidationError(
                "employeeId", true, null,
                "Must provide employee to book appointment with via Employee ID.");

        if (request.time() == null) ve.addValidationError(
            "time", true, null,
            "Must provide appointment time.");
        else if (request.time().isBefore(Instant.now().atZone(UTC))) ve.addValidationError(
            "time", true, request.time().toString(),
            "Appointments can not take place in the past!");
        else if (    request.time().getMinute()!=0
                &&request.time().getMinute()!=15
                && request.time().getMinute()!=30
                && request.time().getMinute()!=45) ve.addValidationError(
            "time", true, request.time().toString(),
            "Appointments should start in increments of 15 minutes (ie 7:00, 7:15, 7:30 or 7:45).");

        if (ve.containsErrors()) throw ve;
    }

    public static void validateAppointmentOpeningsRequest(AppointmentRequest request) throws ValidationException {

        ValidationException ve = new ValidationException();

        if (request.serviceId() == null) ve.addValidationError(
                "serviceId", true, null,
                "Must provide type of appointment via Service ID.");

        if (request.employeeId() == null) ve.addValidationError(
                "employeeId", true, null,
                "Must provide employee to book appointment with via Employee ID.");

        if (request.searchDate() == null) ve.addValidationError(
                "searchDate", true, null,
                "Must provide search date.");
        else if (request.searchDate()
                .isBefore(Instant.now().atZone(UTC).truncatedTo(ChronoUnit.DAYS))) ve.addValidationError(
                "searchDate", true, request.searchDate().toString(),
                "Appointments can not take place in the past!");


        if (ve.containsErrors()) throw ve;
    }
}
