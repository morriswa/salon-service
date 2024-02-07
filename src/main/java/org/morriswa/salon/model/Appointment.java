package org.morriswa.salon.model;

import java.time.ZonedDateTime;

public record Appointment(
    Long appointmentId,
    Long serviceId,
    ZonedDateTime time
) { } 