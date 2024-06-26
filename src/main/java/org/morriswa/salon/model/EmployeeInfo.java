package org.morriswa.salon.model;

import lombok.Getter;

@Getter
public class EmployeeInfo extends UserInfo {
    private final String bio;

    public EmployeeInfo(
            String firstName, String lastName, String pronouns,
            String phoneNumber, String email,
            String addressLineOne, String addressLineTwo, String city, String stateCode, String zipCode,
            String contactPreference,
            String bio) {
        super(  firstName, lastName, pronouns,
                phoneNumber, email, addressLineOne, addressLineTwo, city, stateCode, zipCode,
                contactPreference);

        this.bio = bio;
    }
}
