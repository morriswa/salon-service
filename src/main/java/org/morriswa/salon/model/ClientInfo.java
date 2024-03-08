package org.morriswa.salon.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ClientInfo extends UserInfo {

    private final LocalDate birthday;

    public ClientInfo(
            String firstName, String lastName, String pronouns,
            String phoneNumber, String email,
            String addressLineOne, String addressLineTwo, String city, String stateCode, String zipCode,
            String contactPreference,
            LocalDate birthday
    ) {

        super(  firstName, lastName, pronouns,
                phoneNumber, email,
                addressLineOne, addressLineTwo, city, stateCode, zipCode,
                contactPreference);

        this.birthday = birthday;
    }
}
