package org.morriswa.salon.dao;

import org.junit.jupiter.api.Test;
import org.morriswa.salon.model.AppointmentRequest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.util.AssertionErrors.*;


public class ScheduleDaoTest extends DaoTest {

    @Test
    public void canRetrieveTomorrowsSchedule() throws Exception {
        final var request = new AppointmentRequest(
                251L,
                25L,
                Instant.now().plus(1, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()),
                null, null, null, null);

        var openings = scheduleDao.retrieveAppointmentOpenings(15L, request);

        assertNotNull("retrieved list should not be null", openings);
        assertFalse("list should not be empty", openings.isEmpty());
        for (var opening : openings) {
            assertNotNull("openings should have a time", opening.time());
            assertEquals("openings should have correct length", 15, opening.length());
        }
    }
}
