package org.morriswa.salon.model;

import org.morriswa.salon.validation.StrTools;

import java.time.ZonedDateTime;

public record UserProfileResponse(
    Long userId, String username, ZonedDateTime accountCreationDate,
    String firstName, String lastName, String address, 
    String phoneNumber, String email, String contactPreference
) { 
    public UserProfileResponse(UserAccount account, ContactInfo info) {
        this(   account.getUserId(), account.getUsername(), account.getDateCreated(), 
                info.firstName(), info.lastName(), 
                String.format("%s %s%s, %s %s", info.addressLineOne(), 
                    StrTools.hasValue(info.addressLineTwo())?info.addressLineTwo()+" ":"",
                    info.city(), info.stateCode(), info.zipCode()),
                String.format("+1 (%s) %s-%s", 
                    info.phoneNumber().substring(0, 3),
                    info.phoneNumber().substring(3,6),
                    info.phoneNumber().substring(6)), 
                info.email(), info.contactPreference()
            );
    };
}
