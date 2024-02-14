package org.morriswa.salon.model;

import java.time.ZonedDateTime;

public record AppointmentOpening(
    ZonedDateTime time,
    Integer length
) { }