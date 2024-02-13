package org.morriswa.salon.model;

import java.time.ZonedDateTime;

public record AppointmentRequest(
    Long serviceId,
    Long employeeId,
    ZonedDateTime appointmentTime
) { }