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
        return switch (code) {
            case "EMAIL" -> Email;
            case "PCALL" -> PhoneCall;
            case "PTEXT" -> TextMessage;
            default -> null;
        };
    }
}
