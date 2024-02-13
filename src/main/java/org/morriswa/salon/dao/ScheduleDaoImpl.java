package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
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

    private int retrieveAppointmentLength(Long serviceId) throws BadRequestException {

        final var queryLength = """
            select default_length
            from provided_service
            where service_id=:serviceId
            """;

        final var params = new HashMap<String, Object>(){{
            put("serviceId", serviceId);
        }};

        try {
            Integer appointmentLength = database.query(queryLength, params, rs -> {
                if (rs.next()) return rs.getInt("default_length");
                else throw new SQLException("Service not found");
            });
            assert appointmentLength != null;
            return appointmentLength;
        } catch (Exception e) {
            throw new BadRequestException(e.getCause().getMessage());
        }

    }

    @Override
    public List<AppointmentLength> checkAvailableTimes(AppointmentRequest request) throws BadRequestException {

        final var query = """
            select
                appointment_time,
                length
            from appointment
            where employee_id=:employeeId
            and
                appointment_time between :startSearch and :endSearch
            """;

        final var startSearch = request.appointmentTime().truncatedTo(ChronoUnit.DAYS);
        final var stopSearch = request.appointmentTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);

        final var params = new HashMap<String, Object>(){{
            put("employeeId", request.employeeId());
            put("startSearch", startSearch);
            put("endSearch", stopSearch);
        }};

        List<AppointmentLength> takenTimes = database.query(query, params, rs -> {
            var appts = new ArrayList<AppointmentLength>();

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

        int appointmentLength = retrieveAppointmentLength(request.serviceId());


        var availableTimes = new ArrayList<AppointmentLength>();

        assert takenTimes != null;

        takenTimes.sort(Comparator.comparing(AppointmentLength::appointmentTime));

        ZonedDateTime scanStart = null;
        ZonedDateTime dayEnd = null;


        if (takenTimes.isEmpty()) {

            // if day has not started mark opening time as first available slot
            scanStart = LocalDateTime.of(
                            LocalDate.from(startSearch),
                            LocalTime.NOON.minusHours(3))
                    .atZone(ZoneId.systemDefault());

            // if day has not ended mark closing time as last available slot
            dayEnd = LocalDateTime.of(
                            LocalDate.from(startSearch),
                            LocalTime.NOON.plusHours(5))
                    .atZone(ZoneId.systemDefault());


            // find the length between current time and end of day
            Integer timeTilEndofDay =
                    (int) Duration.between(scanStart, dayEnd).toMinutes();

            int slots = (timeTilEndofDay / 15) - appointmentLength + 1;
            for (int i = 0; i < slots; i++) {
                availableTimes.add(new AppointmentLength(
                        scanStart,
                        appointmentLength * 15)
                );
                scanStart = scanStart.plusMinutes(15);
            }
        } else for (int currentAptIdx = 0; currentAptIdx < takenTimes.size(); currentAptIdx++) {

            // if day has not started mark opening time as first available slot
            if (scanStart == null) scanStart = LocalDateTime.of(
                            LocalDate.from(takenTimes.get(currentAptIdx).appointmentTime()),
                            LocalTime.NOON.minusHours(3))
                    .atZone(ZoneId.systemDefault());

            // if day has not ended mark closing time as last available slot
            dayEnd = LocalDateTime.of(
                                LocalDate.from(takenTimes.get(currentAptIdx).appointmentTime()),
                                LocalTime.NOON.plusHours(5))
                        .atZone(ZoneId.systemDefault());

            // find the length in minutes between current time and next taken appointment
            Integer timeTilNextAppointment = (int)
                    Duration.between(scanStart, takenTimes.get(currentAptIdx).appointmentTime()).toMinutes();

            // find the length between current time and end of day
            Integer timeTilEndofDay =
                    (int) Duration.between(scanStart, dayEnd).toMinutes();

            // if current slot will not be long enough
            if (timeTilNextAppointment < (appointmentLength * 15)) {
                // iterate
                scanStart = takenTimes.get(currentAptIdx)
                        .appointmentTime()
                        .plusMinutes(takenTimes
                                .get(currentAptIdx)
                                .appointmentLength() * 15L);

            // if there are any more appointments happening before the end of day
            } else if (timeTilNextAppointment <= timeTilEndofDay ) {

                // record open slots
                int slots = (timeTilNextAppointment / 15) - appointmentLength + 1;
                for (int i = 0; i < slots; i++) {
                    availableTimes.add(new AppointmentLength(
                            scanStart,
                            appointmentLength * 15)
                    );
                    scanStart = scanStart.plusMinutes(15);
                }

                // iterate
                scanStart = takenTimes.get(currentAptIdx).appointmentTime()
                        .plusMinutes(takenTimes.get(currentAptIdx).appointmentLength() * 15L);

                // check if there are any more appointments on the schedule
                // if so mark down available time
                timeTilNextAppointment = currentAptIdx + 1 == takenTimes.size()?
                        null :
                        (int) Duration.between(scanStart, takenTimes.get(currentAptIdx+1).appointmentTime()).toMinutes();
                // update end of day
                timeTilEndofDay =
                        (int) Duration.between(scanStart, dayEnd).toMinutes();

                // if there are no more appointments on the schedule for the current day
                if (timeTilNextAppointment==null||timeTilNextAppointment >= timeTilEndofDay) {

                    // record open slot until days end
                    slots = (timeTilEndofDay / 15) - appointmentLength + 1;

                    for (int i = 0; i < slots; i++) {
                        availableTimes.add(new AppointmentLength(
                                scanStart,
                                appointmentLength * 15)
                        );
                        scanStart = scanStart.plusMinutes(15);
                    }
                }
            }
        }

        return availableTimes;

    }

    @Override
    public void registerAppointment(Long clientId, AppointmentRequest request) throws BadRequestException {

        if (request.appointmentTime().isBefore(Instant.now().atZone(ZoneId.systemDefault())))
            throw new BadRequestException("Appointments can not take place in the past!");

        int startMinute = request.appointmentTime().getMinute();

        if (    startMinute!=0
                &&startMinute!=15
                && startMinute!=30
                && startMinute!=45)
            throw new BadRequestException("Appointments should start in increments of 15 minutes");

        int appointmentLength = retrieveAppointmentLength(request.serviceId());

        final var query = """
            select
                appointment_time,
                length
            from appointment
            where employee_id=:employeeId
            and
                appointment_time between :startSearch and :endSearch
            """;

        final var startSearch = request.appointmentTime();
        final var stopSearch = request.appointmentTime().plusMinutes(appointmentLength * 15L).minusMinutes(1);
        final var params2 = new HashMap<String, Object>(){{
            put("employeeId", request.employeeId());
            put("startSearch", startSearch);
            put("endSearch", stopSearch);
        }};

        boolean allowed = Boolean.TRUE.equals(database.query(query, params2, rs -> !rs.next()));
        if (!allowed) throw new BadRequestException("Time is unavailable!");


        final var addQuery = """
            insert into appointment (client_id, employee_id, appointment_time, service_id, date_due, length)
            values (:clientId, :employeeId, :appointmentTime, :serviceId, :due, :length)
            """;

        final var addParams = new HashMap<String, Object>(){{
            put("clientId", clientId);
            put("employeeId", request.employeeId());
            put("appointmentTime", request.appointmentTime());
            put("serviceId", request.serviceId());
            put("due", request.appointmentTime().plusWeeks(2));
            put("length", appointmentLength);
        }};

        database.update(addQuery, addParams);
    }
}
