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
                info.contactInfo().firstName(), info.contactInfo().lastName(), info.contactInfo().pronouns(),
                String.format("%s %s%s, %s %s", info.contactInfo().addressLineOne(),
                        StrTools.hasValue(info.contactInfo().addressLineTwo())?info.contactInfo().addressLineTwo()+" ":"",
                        info.contactInfo().city(), info.contactInfo().stateCode(), info.contactInfo().zipCode()),
                info.contactInfo().phoneNumber(), info.contactInfo().email(), info.contactInfo().contactPreference(),
                info.bio(), profileImage
        );
    };
}
