package org.morriswa.salon.validation;

import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.AppointmentRequest;

public class ClientValidator {
    public static void validateSeeAvailableTimesRequestOrThrow(AppointmentRequest request) throws ValidationException {

        var ve = new ValidationException();

        if (request.serviceId()==null) ve.addValidationError(
            "serviceId",true,null, "Service ID is a required field!");
        if (request.employeeId()==null) ve.addValidationError(
                "employeeId",true,null, "Employee ID is a required field!");
        if (request.searchDate()==null) ve.addValidationError(
                "searchDate",true,null, "Search Date is a required field!");
        if (request.timeZone()==null) ve.addValidationError(
                "timeZone",true,null, "Time Zone is a required field!");

        if (ve.containsErrors()) throw ve;
    }

    public static void validateRegisterAppointmentRequestOrThrow(AppointmentRequest request) throws ValidationException {

        var ve = new ValidationException();

        if (request.serviceId()==null) ve.addValidationError(
                "serviceId",true,null, "Service ID is a required field!");
        if (request.employeeId()==null) ve.addValidationError(
                "employeeId",true,null, "Employee ID is a required field!");
        if (request.searchDate()==null) ve.addValidationError(
                "appointmentTime",true,null, "Appointment Time is a required field!");
        if (request.timeZone()==null) ve.addValidationError(
                "timeZone",true,null, "Time Zone is a required field!");

        if (ve.containsErrors()) throw ve;
    }
}
