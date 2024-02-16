package org.morriswa.salon.model;

import org.morriswa.salon.enumerated.AppointmentStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record Appointment(
    Long appointmentId,
    ZonedDateTime time,
    Integer length,
    ZonedDateTime dateCreated,
    ZonedDateTime paymentDueDate,
    BigDecimal cost,
    BigDecimal tipAmount,
    AppointmentStatus status,
    ServiceInfo service,
    EmployeeInfo employee,
    ClientInfo client
) {
    private record ClientInfo (
        Long clientId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String contactPreference
    ) {  }

    private record EmployeeInfo (
        Long employeeId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String contactPreference
    ) {  }

    private record ServiceInfo (
        Long serviceId,
        String name
    ) {  }
}