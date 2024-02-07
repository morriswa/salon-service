package org.morriswa.salon.enumerated;

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

    public static ContactPreference getEnum(String code) {
        switch (code) {
            case "EMAIL":
                return Email;
            case "PCALL":
                return PhoneCall;
            case "PTEXT":
                return TextMessage;
            default:
                return null;
        }
    }
}
