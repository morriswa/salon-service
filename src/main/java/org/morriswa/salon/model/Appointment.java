package org.morriswa.salon.model;

import org.morriswa.salon.enumerated.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Appointment(
    Long appointmentId,
    Long serviceId,
    Long employeeId,
    LocalDateTime appointmentTime,
    LocalDateTime dateCreated,
    LocalDateTime paymentDueDate,
    ClientInfo client,
    BigDecimal appliedDiscount,
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
}