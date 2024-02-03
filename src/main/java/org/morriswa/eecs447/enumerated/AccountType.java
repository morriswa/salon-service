package org.morriswa.eecs447.enumerated;

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
        switch (code) {
            case "USR":
                return User;
            case "EMP":
                return Employee;
            case "ADM":
                return Admin;
            case "CLT":
                return Client;
            default:
                return null;
        }
    }
}
