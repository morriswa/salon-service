package org.morriswa.eecs447.enumurated;

public enum UserContactPreferences {
    Email("EMAIL"),
    PhoneCall("PCALL"),
    TextMessage("PTEXT");


    public final String databaseCode;

    UserContactPreferences(String databaseCode) {
        this.databaseCode = databaseCode;
    }
}
