package org.morriswa.salon.model;

import lombok.Getter;

import java.net.URL;

@Getter
public class EmployeeProfileResponse extends EmployeeInfo {
    private final URL profileImage;


    public EmployeeProfileResponse(EmployeeInfo info, URL profileImage) {

        super(  info.getFirstName(), info.getLastName(), info.getPronouns(),
                info.getPhoneNumber(), info.getEmail(),
                info.getAddressLineOne(), info.getAddressLineTwo(), info.getCity(),
                info.getStateCode(), info.getZipCode(),
                info.getContactPreference(), info.getBio());

        this.profileImage = profileImage;
    }
}
