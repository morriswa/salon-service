package org.morriswa.salon.utility;

import java.time.LocalTime;
import java.time.ZoneId;

public interface TimeZoneUtil {
    ZoneId getZoneOfSalon();

    LocalTime getSalonOpen();

    LocalTime getSalonClose();
}
