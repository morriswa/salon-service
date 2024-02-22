package org.morriswa.salon.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TimeZoneUtilImpl implements TimeZoneUtil{
    private final ZoneId SALON_TIME_ZONE;
    private final LocalTime SALON_OPEN;
    private final LocalTime SALON_CLOSE;
    private final ZoneId UTC = ZoneId.of("+00:00");

    private final Logger log = LoggerFactory.getLogger(TimeZoneUtilImpl.class);


    @Autowired
    public TimeZoneUtilImpl(
            Environment environment) {
        // retrieve all time related configuration about salon
        final var tzString = environment.getRequiredProperty("salon.timezone");
        final var openString = environment.getRequiredProperty("salon.hours.open").trim().toUpperCase();
        final var closeString = environment.getRequiredProperty("salon.hours.close").trim().toUpperCase();

        // initialize time settings
        SALON_TIME_ZONE = ZoneId.of(tzString);
        SALON_OPEN = LocalTime.parse(openString, DateTimeFormatter.ofPattern("h:mm a"));
        SALON_CLOSE = LocalTime.parse(closeString, DateTimeFormatter.ofPattern("h:mm a"));

        // print output on success
        log.info("Starting service for a salon in timezone {} that opens at {} and closes at {}",
                SALON_TIME_ZONE, SALON_OPEN, SALON_CLOSE);

    }

    @Override
    public ZoneId getZoneOfSalon() {
        return SALON_TIME_ZONE;
    }

    @Override
    public LocalTime getSalonOpen() {
        return SALON_OPEN;
    }

    @Override
    public LocalTime getSalonClose() {
        return SALON_CLOSE;
    }
}
