package org.morriswa.salon.dao;

import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.model.AppointmentLength;
import org.morriswa.salon.model.AppointmentRequest;
import org.morriswa.salon.model.ProvidedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
public class ScheduleDaoImpl  implements ScheduleDao{

    // this app was designed to support one salon
    // these variables define important salon rules
    private final ZoneId SALON_TIME_ZONE;
    private final LocalTime SALON_OPEN;
    private final LocalTime SALON_CLOSE;
    private final ZoneId UTC = ZoneId.of("+00:00");

    private final NamedParameterJdbcTemplate database;
    private final Logger log = LoggerFactory.getLogger(ScheduleDaoImpl.class);


    @Autowired
    public ScheduleDaoImpl(
            Environment environment,
            NamedParameterJdbcTemplate database) {
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

        this.database = database;
    }


    private ProvidedService retrieveProvidedService(Long serviceId) throws BadRequestException {

        final var queryLength = """
            select *
            from provided_service
            where service_id=:serviceId
            """;

        final var params = new HashMap<String, Object>(){{
            put("serviceId", serviceId);
        }};

        try {
            return database.query(queryLength, params, rs -> {
                if (rs.next()) return new ProvidedService(
                        rs.getLong("service_id"),
                        rs.getBigDecimal("default_cost"),
                        rs.getInt("default_length"),
                        rs.getString("provided_service_name"));
                else throw new SQLException("Service not found");
            });
        } catch (Exception e) {
            throw new BadRequestException(e.getCause().getMessage());
        }

    }

    @Override
    public List<AppointmentLength> checkAvailableTimes(AppointmentRequest request) throws BadRequestException {

        // get end of day in current timezone, mark as end of search
        final var stopSearch = request.searchDate().atTime(23,59)
                .atZone(SALON_TIME_ZONE);

        // retrieve the current time
        var currentTime = Instant.now()
                .atZone(SALON_TIME_ZONE);

        // if the current time is after the end of search boundary, throw appropriate exception.
        if (currentTime.isAfter(stopSearch)) throw new BadRequestException("Appointments can not take place in the past!");

        // get beginning of day in current timezone
        var dayStart = request.searchDate().atTime(0,0)
                .atZone(SALON_TIME_ZONE);

        // if the current time is within the day to search, use current time as start search
        // else use start of day as start of search
        final var startSearch = dayStart.isAfter(currentTime)?
                dayStart : currentTime;

        // defn search query
        final var query = """
            select
                appointment_time,
                length
            from appointment
            where employee_id=:employeeId
            and
                appointment_time between :startSearch and :endSearch
            """;

        // inject params
        final var params = new HashMap<String, Object>(){{
            put("employeeId", request.employeeId());
            put("startSearch", startSearch);
            put("endSearch", stopSearch);
        }};

        // retrieve all preexisting appointments
        List<AppointmentLength> preexistingAppointments = database.query(query, params, rs -> {
            var appts = new ArrayList<AppointmentLength>();

            while (rs.next()) appts.add(new AppointmentLength(
                // retrieve every appointment's time in current time zone
                rs.getTimestamp("appointment_time")
                        .toInstant()
                        .atZone(request.timeZone()),
                // convert length to minutes
                rs.getInt("length") * 15
            ));

            return appts;
        });
        // this list should never be null
        assert preexistingAppointments != null;

        // retrieve the length of requested service type
        int appointmentLength = retrieveProvidedService(request.serviceId()).defaultLength();

        // create list to store available appointment times
        var availableTimes = new ArrayList<AppointmentLength>();

        // ensure existing appointments are stored in correct order
        preexistingAppointments.sort(Comparator.comparing(AppointmentLength::appointmentTime));

        // record time salon opens and closes, in client's timezone
        final var salonOpen = LocalDateTime.of(request.searchDate(), SALON_OPEN)
                .atZone(SALON_TIME_ZONE).toInstant().atZone(request.timeZone());

        final var salonClose = LocalDateTime.of(request.searchDate(), SALON_CLOSE)
                .atZone(SALON_TIME_ZONE).toInstant().atZone(request.timeZone());

        // record first available appointment time
        // (at least one hour ahead of the present to avoid last minute bookings)
        final var firstAvailableAppointment = Instant.now().atZone(request.timeZone())
                .plusMinutes(61).truncatedTo(ChronoUnit.HOURS);

        // define var to keep track of time scanner is currently at
        ZonedDateTime scanner = null;

        // if there are no preexisting appointments on requested day
        if (preexistingAppointments.isEmpty()) {

            // start scanning for available times at first available appointment
            // or salon open time if search is in the future
            scanner = currentTime.isAfter(salonOpen)?
                    firstAvailableAppointment :
                    salonOpen;

            // find the length between current time and end of day
            int timeTilEndofDay =
                    (int) Duration.between(scanner, salonClose).toMinutes();

            // determine how many slots are available in current day
            int slots = (timeTilEndofDay / 15) - appointmentLength + 1;
            for (int i = 0; i < slots; i++) {

                // add every open slot to available times
                availableTimes.add(new AppointmentLength(
                        scanner,
                        appointmentLength * 15)
                );

                // and increment by slot length (15 minutes)
                scanner = scanner.plusMinutes(15);
            }
        // if there ARE existing appointments on requested day...
        // iterate through all existing appointments
        } else for (int existingAppointmentIdx = 0;
                    existingAppointmentIdx < preexistingAppointments.size();
                    existingAppointmentIdx++) {

            // if scanner has not been initialized,
            if (scanner == null)
                // start scanning for available times at first available appointment
                // or salon open time if search day is in the future
                scanner = currentTime.isAfter(salonOpen)?
                        firstAvailableAppointment :
                        salonOpen;

            // find the length in minutes between current time and next taken appointment
            Integer timeTilNextAppointment = (int)
                    Duration.between(scanner, preexistingAppointments.get(existingAppointmentIdx).appointmentTime()).toMinutes();

            // find the length between current time and end of day
            int timeTilEndofDay =
                    (int) Duration.between(scanner, salonClose).toMinutes();

            // if current slot will not be long enough to fit requested appointment type
            if (timeTilNextAppointment < (appointmentLength * 15)) {
                // move scanner to end of next appointment
                scanner = preexistingAppointments.get(existingAppointmentIdx)
                        .appointmentTime()
                        .plusMinutes(preexistingAppointments
                                .get(existingAppointmentIdx)
                                .appointmentLength());

            // if current slot will be long enough
            // and there are more appointments happening before the end of day
            } else if (timeTilNextAppointment <= timeTilEndofDay) {

                // find number of open slots
                int slots = (timeTilNextAppointment / 15) - appointmentLength + 1;
                for (int i = 0; i < slots; i++) {

                    // add every open slot to available times
                    availableTimes.add(new AppointmentLength(
                            scanner,
                            appointmentLength * 15)
                    );

                    // and increment by slot length (15 minutes)
                    scanner = scanner.plusMinutes(15);
                }

                // move scanner to end of next appointment
                scanner = preexistingAppointments.get(existingAppointmentIdx).appointmentTime()
                        .plusMinutes(preexistingAppointments.get(existingAppointmentIdx).appointmentLength());
            }

            // if there are no more appointments on the schedule,
            if (existingAppointmentIdx + 1 == preexistingAppointments.size()) {

                // update time til end of day
                timeTilEndofDay =
                        (int) Duration.between(scanner, salonClose).toMinutes();

                // record open slot until days end
                int slots = (timeTilEndofDay / 15) - appointmentLength + 1;
                for (int i = 0; i < slots; i++) {

                    // add every open slot to available times
                    availableTimes.add(new AppointmentLength(
                            scanner,
                            appointmentLength * 15)
                    );

                    // and increment by slot length (15 minutes)
                    scanner = scanner.plusMinutes(15);
                }
            }

        }

        return availableTimes;

    }

    @Override
    public void registerAppointment(Long clientId, AppointmentRequest request) throws BadRequestException {

        if (request.appointmentTime().atZone(request.timeZone())
                .isBefore(Instant.now().atZone(UTC)))
            throw new BadRequestException("Appointments can not take place in the past!");

        int startMinute = request.appointmentTime().getMinute();

        if (    startMinute!=0
                &&startMinute!=15
                && startMinute!=30
                && startMinute!=45)
            throw new BadRequestException("Appointments should start in increments of 15 minutes");

        ProvidedService serviceToSchedule = retrieveProvidedService(request.serviceId());

        if (request.appointmentTime().atZone(request.timeZone()).plusMinutes(
                serviceToSchedule.defaultLength() * 15L
        ).isAfter(
                LocalDateTime.of(
                                LocalDate.from(request.appointmentTime()),
                                SALON_CLOSE)
                        .atZone(SALON_TIME_ZONE)
        )) throw new BadRequestException("Appointments should not end after salon close!");
        else if (request.appointmentTime().atZone(request.timeZone()).isBefore(
                LocalDateTime.of(
                                LocalDate.from(request.appointmentTime()),
                                SALON_OPEN)
                        .atZone(SALON_TIME_ZONE)
        )) throw new BadRequestException("Appointments should not start before salon opens!");


        final var query = """
            select
                appointment_time,
                length
            from appointment
            where employee_id=:employeeId
            and
                appointment_time between :startSearch and :endSearch
            """;

        final var startSearch = request.appointmentTime()
                .atZone(request.timeZone());
        final var stopSearch = request.appointmentTime()
                .atZone(request.timeZone())
                .plusMinutes(serviceToSchedule.defaultLength() * 15L).minusMinutes(1);
        final var params2 = new HashMap<String, Object>(){{
            put("employeeId", request.employeeId());
            put("startSearch", startSearch);
            put("endSearch", stopSearch);
        }};

        boolean allowed = Boolean.TRUE.equals(database.query(query, params2, rs -> !rs.next()));
        if (!allowed) throw new BadRequestException("Time is unavailable!");


        final var addQuery = """
            insert into appointment
                (client_id, employee_id, appointment_time,
                    service_id, actual_amount, date_due, length)
            values
                (:clientId, :employeeId, :appointmentTime,
                    :serviceId, :actualAmount, :due, :length)
            """;

        final var addParams = new HashMap<String, Object>(){{
            put("clientId", clientId);
            put("employeeId", request.employeeId());
            put("appointmentTime", request.appointmentTime().atZone(request.timeZone()));
            put("serviceId", request.serviceId());
            put("actualAmount", serviceToSchedule.defaultCost());
            put("due", request.appointmentTime().atZone(request.timeZone()).plusWeeks(2));
            put("length", serviceToSchedule.defaultLength());
        }};

        database.update(addQuery, addParams);
    }
}
