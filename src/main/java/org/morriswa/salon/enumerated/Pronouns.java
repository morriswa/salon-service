package org.morriswa.salon.enumerated;

public enum Pronouns {
    HE(DatabaseCodes.Pronouns.he, "he/him/his"),
    SHE(DatabaseCodes.Pronouns.she, "she/her/hers"),
    THEY(DatabaseCodes.Pronouns.they, "they/them/theirs");


    public final String code;
    public final String description;

    Pronouns(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Pronouns getEnum(String code) {
        return switch (code) {
            case DatabaseCodes.Pronouns.he -> Pronouns.HE;
            case DatabaseCodes.Pronouns.she -> Pronouns.SHE;
            case DatabaseCodes.Pronouns.they -> Pronouns.THEY;
            default -> null;
        };
    }

    public static String getPronounStr(String code) {
        final var pronoun = getEnum(code);
        return pronoun==null ? null: pronoun.description;
    }
}
