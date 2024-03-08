package org.morriswa.salon.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProvidedServiceDetails extends ProvidedService {

    public record EmployeeInfo (
            Long employeeId,
            String firstName,
            String lastName,
            String pronouns,
            String phoneNumber,
            String email,
            String contactPreference
    ) {  }

    private final EmployeeInfo employee;

    public ProvidedServiceDetails(Long serviceId, BigDecimal defaultCost, Integer defaultLength, String name,
                                  EmployeeInfo employee) {
        super(serviceId, defaultCost, defaultLength, name);
        this.employee = employee;
    }


}
