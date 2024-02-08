package org.morriswa.salon.enumerated;

public enum AccountType {
    User("USR"),
    Client("CLT"),
    Employee("EMP"),
    Admin("ADM");

    public final String code;

    AccountType(String code) {
        this.code = code;
    }

    public static AccountType getEnum(String code) {
        return switch (code) {
            case "USR" -> User;
            case "EMP" -> Employee;
            case "ADM" -> Admin;
            case "CLT" -> Client;
            default -> null;
        };
    }
}
