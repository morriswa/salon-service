package org.morriswa.salon.model;

import java.net.URL;

public record PublicEmployeeProfileResponse(
        String firstName, String lastName, String pronouns,
        String phoneNumber, String email, String contactPreference,
        String bio, URL profileImage
) {
    public PublicEmployeeProfileResponse(EmployeeInfo info, URL employeeProfileImage) {
        this(
                info.getFirstName(), info.getLastName(), info.getPronouns(),
                info.getPhoneNumber(), info.getEmail(), info.getContactPreference(),
                info.getBio(), employeeProfileImage
        );
    }
}
