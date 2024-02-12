package org.morriswa.salon.model;

import org.morriswa.salon.enumerated.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record AppointmentRequest(
    Long serviceId,
    Long employeeId,
    ZonedDateTime appointmentTime
) { }