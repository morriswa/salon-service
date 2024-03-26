package org.morriswa.salon.enumerated;

public enum Pronouns {
    HE(DatabaseCodes.Pronouns.he, "He/Him/His"),
    SHE(DatabaseCodes.Pronouns.she, "She/Her/Hers"),
    THEY(DatabaseCodes.Pronouns.they, "They/Them/Theirs"),
    PREFER_NOT_SAY(DatabaseCodes.Pronouns.not_say, "Prefer not to say");


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
            case DatabaseCodes.Pronouns.not_say -> Pronouns.PREFER_NOT_SAY;
            default -> null;
        };
    }

    public static String getPronounStr(String code) {
        final var pronoun = getEnum(code);
        return pronoun==null ? null: pronoun.description;
    }
}
