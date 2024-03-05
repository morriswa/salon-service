package org.morriswa.salon.model;


import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record EmployeeInfo (
        @JsonUnwrapped ContactInfo contactInfo,
        String bio
) {

}
