package org.morriswa.salon.model;

public record ContactInfo (
    String firstName,
    String lastName, 
    String phoneNumber,
    String email,
    String addressLineOne,
    String addressLineTwo,
    String city,
    String stateCode,
    String zipCode,
    String contactPreference
) { }
