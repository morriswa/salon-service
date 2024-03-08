package org.morriswa.salon.model;

import lombok.Getter;
import org.morriswa.salon.validation.StrTools;

@Getter
public class UserInfo {
    private final String firstName;
    private final String lastName;
    private final String pronouns;
    private final String phoneNumber;
    private final String email;
    private final String addressLineOne;
    private final String addressLineTwo;
    private final String city;
    private final String stateCode;
    private final String zipCode;
    private final String formattedAddress;
    private final String contactPreference;

    public UserInfo(String firstName, String lastName, String pronouns,
                    String phoneNumber, String email,
                    String addressLineOne, String addressLineTwo, String city, String stateCode, String zipCode,
                    String contactPreference)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pronouns = pronouns;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.city = city;
        this.stateCode = stateCode;
        this.zipCode = zipCode;
        this.contactPreference = contactPreference;
        this.formattedAddress = String.format("%s %s%s, %s %s", addressLineOne,
                StrTools.hasValue(addressLineTwo) ? addressLineTwo+" " : "",
                city, stateCode, zipCode);
    }
}

