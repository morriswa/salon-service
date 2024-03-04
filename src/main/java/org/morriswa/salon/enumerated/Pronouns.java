package org.morriswa.salon.enumerated;

public enum Pronouns {
    HE("H", "he/him/his"),
    SHE("S", "she/her/hers"),
    THEY("T", "they/them/theirs");


    public final String code;
    public final String description;

    Pronouns(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Pronouns getEnum(String code) {
        return switch (code) {
            case "H" -> Pronouns.HE;
            case "S" -> Pronouns.SHE;
            case "T" -> Pronouns.THEY;
            default -> null;
        };
    }

    public static String getPronounStr(String code) {
        return switch (code) {
            case "H" -> Pronouns.HE.description;
            case "S" -> Pronouns.SHE.description;
            case "T" -> Pronouns.THEY.description;
            default -> null;
        };
    }
}
