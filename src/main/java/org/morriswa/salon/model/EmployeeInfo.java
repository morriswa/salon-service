package org.morriswa.salon.model;


import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record EmployeeInfo (
        String firstName,
        String lastName,
        String pronouns,
        String phoneNumber,
        String email,
        String addressLineOne,
        String addressLineTwo,
        String city,
        String stateCode,
        String zipCode,
        String contactPreference,
        String bio
) {

}
