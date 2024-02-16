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
    String status,
    ServiceInfo service,
    EmployeeInfo employee,
    ClientInfo client
) {
    public record ClientInfo (
        Long clientId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String contactPreference
    ) {  }

    public record EmployeeInfo (
        Long employeeId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String contactPreference
    ) {  }

    public record ServiceInfo (
        Long serviceId,
        String name
    ) {  }
}