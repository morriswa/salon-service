package org.morriswa.eecs447.enumerated;

public enum ContactPreference {
    Email("EMAIL", "Email"),
    PhoneCall("PCALL", "Phone Call"),
    TextMessage("PTEXT", "Text Message");


    public final String code;
    public final String description;

    ContactPreference(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
