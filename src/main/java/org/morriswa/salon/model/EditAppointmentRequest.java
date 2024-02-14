package org.morriswa.salon.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record EditAppointmentRequest(
    LocalDateTime time,
    ZoneId timeZone,
    Integer length,
    BigInteger cost
) { }