package org.morriswa.eecs447.model;

import java.time.ZonedDateTime;

public record Appointment(
    Long appointmentId,
    Long serviceId,
    ZonedDateTime time
) { } 