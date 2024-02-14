package org.morriswa.salon.model;

import java.math.BigDecimal;
import java.time.*;

public record AppointmentRequest(
    Long serviceId,
    Long employeeId,
    LocalDate searchDate,
    LocalDateTime time,
    Integer length,
    BigDecimal cost,
    ZoneId timeZone
) { }