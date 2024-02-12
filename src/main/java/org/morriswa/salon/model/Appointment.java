package org.morriswa.salon.model;

import org.morriswa.salon.enumerated.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record Appointment(
    Long appointmentId,
    ServiceInfo service,
    EmployeeInfo employee,
    ZonedDateTime appointmentTime,
    Integer appointmentLength,
    ZonedDateTime dateCreated,
    ZonedDateTime paymentDueDate,
    ClientInfo client,
    BigDecimal appliedDiscount,
    BigDecimal cost,
    AppointmentStatus status
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