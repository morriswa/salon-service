package org.morriswa.salon.model;

import java.time.*;

public record AppointmentRequest(
    Long serviceId,
    Long employeeId,
    LocalDate searchDate,
    LocalDateTime appointmentTime,
    ZoneId timeZone
) { }