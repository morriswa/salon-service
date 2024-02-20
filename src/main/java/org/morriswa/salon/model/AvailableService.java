package org.morriswa.salon.model;

import java.math.BigDecimal;

public record AvailableService(
    Long serviceId,
    String name,
    BigDecimal cost,
    Integer length,
    EmployeeInfo employee
) {
    public record EmployeeInfo (
        Long employeeId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String contactPreference
    ) {  }
}
