package org.morriswa.salon.enumerated;


public enum AppointmentStatus {
    Good(DatabaseCodes.AppointmentStatus.good),
    Missed(DatabaseCodes.AppointmentStatus.missed),
    Canceled(DatabaseCodes.AppointmentStatus.cancelled);

    public final String code;

    AppointmentStatus(String code) {
        this.code = code;
    }

    public static AppointmentStatus getEnum(String code) {
        return switch (code) {
            case DatabaseCodes.AppointmentStatus.good -> Good;
            case DatabaseCodes.AppointmentStatus.missed -> Missed;
            case DatabaseCodes.AppointmentStatus.cancelled -> Canceled;
            default -> null;
        };
    }
}

