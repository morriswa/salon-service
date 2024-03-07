package org.morriswa.salon.model;

import java.net.URL;

public record PublicEmployeeProfileResponse(
        String firstName, String lastName, String pronouns,
        String phoneNumber, String email, String contactPreference,
        String bio, URL profileImage
) {
    public PublicEmployeeProfileResponse(EmployeeInfo info, URL employeeProfileImage) {
        this(
                info.firstName(), info.lastName(), info.pronouns(),
                info.phoneNumber(), info.email(), info.contactPreference(),
                info.bio(), employeeProfileImage
        );
    }
}
