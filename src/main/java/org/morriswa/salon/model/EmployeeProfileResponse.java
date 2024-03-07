package org.morriswa.salon.model;

import org.morriswa.salon.validation.StrTools;

import java.net.URL;

public record EmployeeProfileResponse(
        String firstName, String lastName, String pronouns,
        String address,
        String phoneNumber, String email, String contactPreference,
        String bio,
        URL profileImage
) {
    public EmployeeProfileResponse(EmployeeInfo info, URL profileImage) {
        this(
                info.firstName(), info.lastName(), info.pronouns(),
                String.format("%s %s%s, %s %s", info.addressLineOne(),
                        StrTools.hasValue(info.addressLineTwo())?info.addressLineTwo()+" ":"",
                        info.city(), info.stateCode(), info.zipCode()),
                info.phoneNumber(), info.email(), info.contactPreference(),
                info.bio(), profileImage
        );
    };
}
