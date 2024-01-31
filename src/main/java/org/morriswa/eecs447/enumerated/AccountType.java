package org.morriswa.eecs447.enumerated;

import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
                return AccountType.User;
            case "EMP":
                return AccountType.Employee;
            case "ADM":
                return AccountType.Admin;
            case "CLT":
                return AccountType.Client;
            default:
                return null;
        }
    }

    public Set<SimpleGrantedAuthority> getAssociatedAuthorities() {
        switch (this) {
            case Admin:
                return Set.of(
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("EMPLOYEE"),
                    new SimpleGrantedAuthority("CLIENT"),
                    new SimpleGrantedAuthority("USER"));
            case Employee:
                return Set.of(
                    new SimpleGrantedAuthority("EMPLOYEE"),
                    new SimpleGrantedAuthority("CLIENT"),
                    new SimpleGrantedAuthority("USER"));
            case Client:
                return Set.of(
                    new SimpleGrantedAuthority("CLIENT"),
                    new SimpleGrantedAuthority("USER"));
            default:
                return Set.of(new SimpleGrantedAuthority("USER"));
        }
    }
}
