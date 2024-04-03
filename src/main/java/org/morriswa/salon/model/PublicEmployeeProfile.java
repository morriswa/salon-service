package org.morriswa.salon.model;

import java.net.URL;

public record PublicEmployeeProfile(
        Long employeeId,
        String firstName, String lastName, String pronouns,
        String phoneNumber, String email, String contactPreference,
        String bio, URL profileImage
) {
    public PublicEmployeeProfile(Long employeeId, EmployeeInfo info, URL employeeProfileImage) {
        this(   employeeId,
                info.getFirstName(), info.getLastName(), info.getPronouns(),
                info.getPhoneNumber(), info.getEmail(), info.getContactPreference(),
                info.getBio(), employeeProfileImage
        );
    }
}
