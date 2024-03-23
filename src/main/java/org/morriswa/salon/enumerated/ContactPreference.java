package org.morriswa.salon.enumerated;

public enum ContactPreference {
    Email(DatabaseCodes.ContactPreference.email, "Email"),
    PhoneCall(DatabaseCodes.ContactPreference.phone, "Phone Call"),
    TextMessage(DatabaseCodes.ContactPreference.text, "Text Message");


    public final String code;
    public final String description;

    ContactPreference(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ContactPreference getEnum(String code) {
        return switch (code) {
            case DatabaseCodes.ContactPreference.email -> Email;
            case DatabaseCodes.ContactPreference.phone -> PhoneCall;
            case DatabaseCodes.ContactPreference.text -> TextMessage;
            default -> null;
        };
    }
}
