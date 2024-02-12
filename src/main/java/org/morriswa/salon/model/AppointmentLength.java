package org.morriswa.salon.model;

import java.time.ZonedDateTime;

public record AppointmentLength(
    ZonedDateTime appointmentTime,
    Integer appointmentLength
) { }