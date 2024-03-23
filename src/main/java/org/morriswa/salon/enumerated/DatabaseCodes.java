package org.morriswa.salon.enumerated;

class DatabaseCodes {
    public static class AppointmentStatus {
        final static String good = "OKGOOD";
        final static String missed = "MISSED";
        final static String cancelled = "CANCEL";
    }

    public static class ContactPreference {
        final static String email = "EMAIL";
        final static String text = "PTEXT";
        final static String phone = "PCALL";
    }

    public static class Pronouns {
        final static String he = "H";
        final static String she = "S";
        final static String they = "T";
    }
}
