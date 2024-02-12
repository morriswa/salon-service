package org.morriswa.salon;

import org.junit.jupiter.api.Test;
import org.morriswa.salon.model.AppointmentLength;

import java.time.*;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Comparator;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class SchedulingInitTest {
    @Test
    public void GetScheduleTestSameDay() {

        var takenTimes = new ArrayList<AppointmentLength>(){{
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON)
                    .atZone(ZoneId.systemDefault()),15));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON.plusHours(1))
                    .atZone(ZoneId.systemDefault()),15));
        }};

        var fatshould = new ArrayList<AppointmentLength>(){{
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON.minusHours(3))
                    .atZone(ZoneId.systemDefault()),180));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON.plusMinutes(15))
                    .atZone(ZoneId.systemDefault()),45));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON.plusHours(1).plusMinutes(15))
                    .atZone(ZoneId.systemDefault()),225));
        }};

        var availableTimes = new ArrayList<AppointmentLength>();

        assert takenTimes != null;

        takenTimes.sort(Comparator.comparing(AppointmentLength::appointmentTime));

        ZonedDateTime scanStart = null;
        ZonedDateTime scanEnd = null;

        for (AppointmentLength takens : takenTimes) {
            // if day has not started mark opening time
            if (scanStart==null) scanStart = LocalDateTime.of(
                            LocalDate.from(takens.appointmentTime()),
                            LocalTime.NOON.minusHours(3))
                    .atZone(ZoneId.systemDefault());

            // if day has not ended mark closing time
            if (scanEnd == null) scanEnd = LocalDateTime.of(
                            LocalDate.from(takens.appointmentTime()),
                            LocalTime.NOON.plusHours(5))
                    .atZone(ZoneId.systemDefault());

            // find the length between current time and next taken appointment
            Integer nextAptLength = (int) Duration.between(scanStart, takens.appointmentTime()).toMinutes();

            if (nextAptLength > 15) {
                if (!availableTimes.isEmpty()) availableTimes.remove(availableTimes.size()-1);
                availableTimes.add(new AppointmentLength(
                        scanStart,
                        nextAptLength)
                );
            }

            // if next appointment ends before the day is over
            var eodCheck = takens.appointmentTime().plusMinutes(takens.appointmentLength());
            if (eodCheck.isBefore(scanEnd)) {
                // generate next appointment
                scanStart = eodCheck;

                Integer dayEndMinutesCrap = (int) Duration.between(scanStart, scanEnd).toMinutes();
                if (dayEndMinutesCrap >= 15) availableTimes.add(new AppointmentLength(scanStart, dayEndMinutesCrap));

            } else {
                scanStart = null;
            }
        }

        assertEquals("dates should be equal", fatshould, availableTimes);
    }

    @Test
    public void GetScheduleTestWeek() {

        var takenTimes = new ArrayList<AppointmentLength>(){{
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON)
                    .atZone(ZoneId.systemDefault()),15));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON.plusHours(1))
                    .atZone(ZoneId.systemDefault()),15));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(1),
                            LocalTime.NOON)
                    .atZone(ZoneId.systemDefault()),15));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(1),
                            LocalTime.NOON.plusHours(1))
                    .atZone(ZoneId.systemDefault()),15));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(2),
                            LocalTime.NOON.minusHours(2))
                    .atZone(ZoneId.systemDefault()),45));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(2),
                            LocalTime.NOON.minusHours(1))
                    .atZone(ZoneId.systemDefault()),45));
        }};

        var fatshould = new ArrayList<AppointmentLength>(){{
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON.minusHours(3))
                    .atZone(ZoneId.systemDefault()),180));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON.plusMinutes(15))
                    .atZone(ZoneId.systemDefault()),45));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.NOON.plusHours(1).plusMinutes(15))
                    .atZone(ZoneId.systemDefault()),225));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(1),
                            LocalTime.NOON.minusHours(3))
                    .atZone(ZoneId.systemDefault()),180));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(1),
                            LocalTime.NOON.plusMinutes(15))
                    .atZone(ZoneId.systemDefault()),45));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(1),
                            LocalTime.NOON.plusHours(1).plusMinutes(15))
                    .atZone(ZoneId.systemDefault()),225));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(2),
                            LocalTime.NOON.minusHours(3))
                    .atZone(ZoneId.systemDefault()),60));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(2),
                            LocalTime.NOON.minusHours(1).minusMinutes(15))
                    .atZone(ZoneId.systemDefault()),15));
            add(new AppointmentLength(LocalDateTime.of(
                            LocalDate.now().plusDays(2),
                            LocalTime.NOON.minusMinutes(15))
                    .atZone(ZoneId.systemDefault()),315));
        }};

        var availableTimes = new ArrayList<AppointmentLength>();

        assert takenTimes != null;

        takenTimes.sort(Comparator.comparing(AppointmentLength::appointmentTime));

        ZonedDateTime scanStart = null;
        ZonedDateTime dayEnd = null;


        for (int currentAptIdx = 0; currentAptIdx < takenTimes.size(); currentAptIdx++) {

            // if day has not started mark opening time as first available slot
            if (scanStart==null) scanStart = LocalDateTime.of(
                    LocalDate.from(takenTimes.get(currentAptIdx).appointmentTime()),
                    LocalTime.NOON.minusHours(3))
                    .atZone(ZoneId.systemDefault());

            // if day has not ended mark closing time as last available slot
            if (dayEnd == null) {
                dayEnd = LocalDateTime.of(
                                LocalDate.from(takenTimes.get(currentAptIdx).appointmentTime()),
                                LocalTime.NOON.plusHours(5))
                        .atZone(ZoneId.systemDefault());
            }

            // find the length in minutes between current time and next taken appointment
            Integer timeTilNextAppointment =
                    (int) Duration.between(scanStart,
                            takenTimes
                                    .get(currentAptIdx)
                                    .appointmentTime()).toMinutes();

            // find the length between current time and end of day
            Integer timeTilEndofDay =
                    (int) Duration.between(scanStart, dayEnd).toMinutes();

            // if current slot will not be long enough
            if (timeTilNextAppointment < 15) {

                // iterate
                scanStart = takenTimes.get(currentAptIdx)
                        .appointmentTime()
                        .plusMinutes(takenTimes
                                .get(currentAptIdx)
                                .appointmentLength());

            // if there are any more appointments happening before the end of day
            } else if (timeTilNextAppointment < timeTilEndofDay) {

                // record open slot
                availableTimes.add(new AppointmentLength(
                        scanStart,
                        timeTilNextAppointment)
                );

                // iterate
                scanStart = takenTimes.get(currentAptIdx).appointmentTime()
                        .plusMinutes(takenTimes.get(currentAptIdx).appointmentLength());;

                // check if there are any more appointments on the schedule
                // if so mark down available time
                timeTilNextAppointment = currentAptIdx + 1 == takenTimes.size()?
                        null :
                        (int) Duration.between(scanStart, takenTimes.get(currentAptIdx+1).appointmentTime()).toMinutes();
                // update end of day
                timeTilEndofDay =
                        (int) Duration.between(scanStart, dayEnd).toMinutes();

                // if there are no more appointments on the schedule for the current day
                if (timeTilNextAppointment==null||timeTilNextAppointment > timeTilEndofDay) {

                    // record open slot until days end
                    availableTimes.add(new AppointmentLength(scanStart, timeTilEndofDay));

                    // reset day counter
                    scanStart = null;
                    dayEnd = null;
                }

            }

        }

        assertEquals("dates should be equal", fatshould, availableTimes);
    }
}
