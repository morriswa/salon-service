package org.morriswa.eecs447.model;

import org.morriswa.eecs447.enumerated.ContactPreference;

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
    ContactPreference contactPreference
) { }
