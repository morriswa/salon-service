package org.morriswa.eecs447.model;

import java.time.ZonedDateTime;

import org.morriswa.eecs447.enumerated.ContactPreference;

public record UserProfileResponse(
    Long userId, String username, ZonedDateTime accountCreationDate,
    String firstName, String lastName, String address, 
    String phoneNumber, String email, String contactPreference
) { 
    public UserProfileResponse(UserAccount account, ContactInfo info) {
        this(   account.getUserId(), account.getUsername(), account.getDateCreated(), 
                info.firstName(), info.lastName(), 
                String.format("%s %s%s, %s %s", info.addressLineOne(), 
                    info.addressLineTwo()==null?"":info.addressLineTwo()+" ",
                    info.city(), info.stateCode(), info.zipCode()),
                String.format("+1 (%s) %s-%s", 
                    info.phoneNumber().substring(0, 3),
                    info.phoneNumber().substring(3,6),
                    info.phoneNumber().substring(6)), 
                info.email(), info.contactPreference()
            );
    };
}
