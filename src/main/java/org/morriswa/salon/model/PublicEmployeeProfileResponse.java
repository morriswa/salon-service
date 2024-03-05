package org.morriswa.salon.model;

import java.net.URL;

public record PublicEmployeeProfileResponse(
        String firstName, String lastName, String pronouns,
        String phoneNumber, String email, String contactPreference,
        String bio, URL profileImage
) {
    public PublicEmployeeProfileResponse(EmployeeInfo info, URL employeeProfileImage) {
        this(
                info.contactInfo().firstName(), info.contactInfo().lastName(), info.contactInfo().pronouns(),
                info.contactInfo().phoneNumber(), info.contactInfo().email(), info.contactInfo().contactPreference(),
                info.bio(), employeeProfileImage
        );
    }
}
