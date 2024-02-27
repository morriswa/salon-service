package org.morriswa.salon.utility;

import java.time.LocalTime;
import java.time.ZoneId;

/**
 * retrieve important time-related salon-specific information
 */
public interface TimeZoneUtil {

    /**
     * To get Salon's configured time zone
     * @return a ZoneId representing the Salon's time-zone
     */
    ZoneId getZoneOfSalon();

    /**
     * To retrieve the time (in local form) the salon will open
     * @return LocalTime of Salon Open
     */
    LocalTime getSalonOpen();

    /**
     * To retrieve the time (in local form) the salon will close
     * @return LocalTime of Salon Close
     */
    LocalTime getSalonClose();
}
