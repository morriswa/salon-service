package org.morriswa.salon.enumerated;

public enum AppointmentStatus {
    Good("OKGOOD"),
    Missed("MISSED"),
    Canceled("CANCEL");
    public final String code;

    AppointmentStatus(String code) {
        this.code = code;
    }

    public static AppointmentStatus getEnum(String code) {
        return switch (code) {
            case "OKGOOD" -> Good;
            case "MISSED" -> Missed;
            case "CANCEL" -> Canceled;
            default -> null;
        };
    }
}
