package org.morriswa.salon.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record Appointment(
    Long appointmentId,
    ZonedDateTime time,
    Integer length,
    ZonedDateTime dateCreated,
    ZonedDateTime paymentDueDate,
    BigDecimal cost,
    BigDecimal tipAmount,
    String status,
    Long serviceId,
    String serviceName,
    UserInfo employee,
    UserInfo client
) {
    public record UserInfo(
        Long userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String contactPreference
    ) {  }
}