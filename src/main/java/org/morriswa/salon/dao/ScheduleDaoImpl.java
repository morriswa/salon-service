package org.morriswa.salon.dao;

import org.morriswa.salon.model.Appointment;
import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
public class ScheduleDaoImpl  implements ScheduleDao{
    private final NamedParameterJdbcTemplate database;

    public ScheduleDaoImpl(NamedParameterJdbcTemplate database) {
        this.database = database;
    }

    @Override
    public List<AppointmentLength> checkAvailableTimes(AppointmentRequest request) {

        final var query = """
            select *
            from appointment
            where employee_id=:employeeId
            and
                appointment_time between :startSearch and :endSearch
            """;

        final var params = new HashMap<String, Object>(){{
            put("employeeId", request.employeeId());
            put("startSearch", request.appointmentTime().minusDays(1));
            put("endSearch", request.appointmentTime().plusDays(3));
        }};

        List<AppointmentLength> takenTimes = database.query(query, params, rs -> {
            List appts = new ArrayList<AppointmentLength>();

            while (rs.next()) {
                appts.add(new AppointmentLength(
                        rs.getTimestamp("appointment_time")
                                .toLocalDateTime()
                                .atZone(ZoneId.systemDefault()),
                        rs.getInt("length")
                ));
            }

            return appts;
        });

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

        return availableTimes;

        

    }
}
