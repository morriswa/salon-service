package org.morriswa.salon.validation;

public class StrTools {

    public static boolean hasValue(String check) {
        return !(check==null||check.isBlank()||check.isEmpty());
    }

    public static boolean isNotNullButBlank(String check) {
        return check!=null&&(check.isBlank()||check.isEmpty());
    }

}
