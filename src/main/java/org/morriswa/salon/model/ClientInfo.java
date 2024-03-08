package org.morriswa.salon.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class ClientInfo extends UserInfo {

    private final Date birthday;

    public ClientInfo(
            String firstName, String lastName, String pronouns,
            String phoneNumber, String email,
            String addressLineOne, String addressLineTwo, String city, String stateCode, String zipCode,
            String contactPreference,
            Date birthday)
    {

        super(  firstName, lastName, pronouns,
                phoneNumber, email,
                addressLineOne, addressLineTwo, city, stateCode, zipCode,
                contactPreference);

        this.birthday = birthday;
    }
}
